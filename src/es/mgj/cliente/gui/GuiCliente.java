package es.mgj.cliente.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.db4o.ObjectSet;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oIOException;

import es.mgj.cliente.beans.ListCellThing;
import es.mgj.ra2.base.Log;
import es.mgj.ra2.base.User;
import es.mgj.util.Constantes;
import es.mgj.util.Util;
import es.mgj.util.Util.Accion;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;

import java.awt.Font;

import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.Color;

public class GuiCliente extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnArchivo;
	private JMenuItem mntmLogin;
	private Socket socket;
	private PrintWriter salida;
	private BufferedReader entrada;
	private boolean conectado;
	private String nick;
	private JScrollPane scrollPane;
	private JList<String> listClientes;
	private DefaultListModel<String> modeloLista;
	private ArrayList<GuiConversacion> listaConversaciones;
	private ArrayList<String> listaIgnorados;
	private JPanel panel;
	private boolean conexionEstablecida;
	private JLabel lblEstadoServidor;
	private JLabel lblEstadoCliente;
	private JMenuItem mntmDesconectar;
	private JLabel lblNombreUsuario;
	private JPopupMenu popupMenu;
	private JMenuItem mntmIgnorar;
	private JMenuItem mntmLogs;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiCliente frame = new GuiCliente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void enviarMensaje(String mensaje, String receptor){
		
		salida.println(this.nick + "/n/nick" + mensaje + "/n/nick" + receptor);
		
	}
	
	public void recibirMensaje(String mensaje){
		
		String[] temp = mensaje.split("/n/nick");
		String emisor = temp[0];
		mensaje = temp[1];
		
		if(listaIgnorados.contains(emisor))
			return;
		
		GuiConversacion gc = abrirConversacion(emisor);

		gc.anadirLinea(emisor, mensaje);
		
	}
	
	public void recibirMensajeDesconexion(String mensaje){
		
		if(this.listaConversaciones == null)
			this.listaConversaciones = new ArrayList<GuiConversacion>();
		
		String[] temp = mensaje.split("/n/nick");
		String emisor = temp[0];
		mensaje = temp[1];
		
		for(GuiConversacion guiConversacion : this.listaConversaciones){
			if(guiConversacion.getNickReceptor().equals(emisor)){
				
				GuiConversacion gc = abrirConversacion(emisor);

				gc.anadirLinea("Servidor", "El usuario " + emisor + " se ha desconectado");
			}
		}
		
		
		
	}
	
	private void login(){
		
		JConecta jc = new JConecta();
		
		if(jc.mostrarDialogo() == Accion.CANCELAR)
			return;
		
		User u = jc.getUser();
		
		if(jc.getAccion() == Accion.REGISTRAR){
			
			Util.db.store(u);
			Util.db.commit();
			
			Log.getLogObject().addEntrada( new GregorianCalendar().getTime() + "/" + "/"
					 + u.getNick() + "/" +  "Registered account");
			
			JOptionPane.showMessageDialog(null, "Cuenta creada con éxito, proceda con el log in");
			
			return;
		}
		
		try {
			
			socket = new Socket(Constantes.HOST, Constantes.PUERTOSOCKET);
			nick = u.getNick();
			lblEstadoCliente.setIcon(new ImageIcon(GuiCliente.class.getResource("/resources/connected.png")));
			this.lblNombreUsuario.setText("Bienvenido " + this.nick);
			
			getMntmLogin().setEnabled(false);
			getMntmDesconectar().setEnabled(true);
			
			conectarServidor();
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
			
	}

	/**
	 * Create the frame.
	 */
	public GuiCliente() {
		setBackground(Color.DARK_GRAY);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GuiCliente.class.getResource("/resources/iChat-Emoticon.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				desconectar();
				System.exit(0);
			}
		});
		setTitle("MChat");
		initialize();
		inicializar();
	}
	
	private void inicializar() {
		
		comprobarConexion();
		
		this.listaIgnorados = new ArrayList<String>();
		
		this.modeloLista = new DefaultListModel<String>();
		this.getListClientes().setModel(modeloLista);
		
	}
	
	private void comprobarConexion(){
		
		Thread hiloConectar = new Thread(new Runnable(){

			@Override
			public void run() {
				
				while(!conexionEstablecida){
					try{
						
						ClientConfiguration configuration = Db4oClientServer.newClientConfiguration();
						configuration.common().updateDepth(2);
						
						Util.db = Db4oClientServer.openClient(configuration, Constantes.HOST, Constantes.PUERTODB4O,
							Constantes.USUARIO, Constantes.CONTRASENA);
						
						conexionEstablecida = true;
						
						Log g = new Log();
										
						ObjectSet<Log> os = Util.db.query(Log.class);
						
						if(os.hasNext()){
							
							Log ng = os.get(0);
							
							ng.addEntrada( new GregorianCalendar().getTime() + "/" + "/" + "/"
									+ "Connection to server");
							
						}else {
							
							g.addEntrada( new GregorianCalendar().getTime() + "/" + "/" + "/"
									+ "Connection to server");
								
						}
							
						
						
					}catch(Db4oIOException dbio){
						
					}
						
				}
				
				lblEstadoServidor.setIcon(
						new ImageIcon(GuiCliente.class.getResource("/resources/Status-user-online-icon.png")));
				mnArchivo.setEnabled(true);
				getMntmLogs().setEnabled(true);
				
				InetAddress equipoRemoto;
				
				try {
					equipoRemoto = InetAddress.getByName(Constantes.HOST);
				
				
					while (true){
						
						if(equipoRemoto.isReachable(3000)){
							lblEstadoServidor.setIcon(
									new ImageIcon(GuiCliente.class.getResource("/resources/Status-user-online-icon.png")));
							
						}else{
							lblEstadoServidor.setIcon(
									new ImageIcon(GuiCliente.class.getResource("/resources/Status-user-bussy-icon.png")));
							
						}
						
						Thread.sleep(5000);
					}
					
				} catch (UnknownHostException e) {
						
						e.printStackTrace();
				} catch (IOException e) {
						
						e.printStackTrace();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			}
			
		});
		
		hiloConectar.start();
		
	}
	
	protected void desconectar() {
		
		if(socket == null)
			return;
			
		salida.println("/quit");
		
		this.mntmLogin.setEnabled(true);
		this.lblEstadoCliente.setIcon(new ImageIcon(GuiCliente.class.getResource("/resources/disconnected.png")));
		this.mntmDesconectar.setEnabled(false);
		this.lblNombreUsuario.setText("");
		this.conectado = false;
		
		this.modeloLista.removeAllElements();
		
		try{
			Util.db.commit();
		}catch(DatabaseClosedException dbc){
			
		}
		
	}
	
	private GuiConversacion abrirConversacion(String emisario){
		
		if(this.listaConversaciones == null)
			this.listaConversaciones = new ArrayList<GuiConversacion>();
			
		for(GuiConversacion gc : this.listaConversaciones){
			
			if(gc.getNickReceptor().equals(emisario)){
				gc.setVisible(true);
				gc.toFront();
				return gc ;
			}
		}
		
		GuiConversacion conversacion = new GuiConversacion(this);

		conversacion.mostrarConversacion(this.nick, emisario);

		this.listaConversaciones.add(conversacion);
		
		return conversacion;
		
	}
	
	public void logConnect(){

		Log.getLogObject().addEntrada( new GregorianCalendar().getTime()  
				+ "/" + socket.getInetAddress()
				+ "/" + this.nick 
				+ "/" + "Log in");
		
	}
	
	private void conectarServidor(){
		
		try {
			this.salida = new PrintWriter(socket.getOutputStream(), true);
			
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.conectado = true;
			
			logConnect();
			
			salida.println(this.nick);
			
			Thread hiloRecibir = new Thread(new Runnable(){
				@Override
				public void run() {
					
					while(conectado){
						
						String linea = "";
						try {
							
							if(socket.isClosed()){
								conectado=false;
								break;
							}
							
							linea = entrada.readLine();
							
							if(linea == null){
								
								continue;
							}
							
							if(linea.startsWith("/s/servidorMessage")){
								JOptionPane.showMessageDialog(null, linea.substring(18));
								desconectar();
								
							}else if(linea.startsWith("/s/servidorNicks")){
								
								listarClientes(linea.substring(16));
								
							}else if(linea.startsWith("/p/ping")){
								
								salida.println("/p/pong");
								
							}
							else if(linea.startsWith("/s/desconectado")){
								
								recibirMensajeDesconexion(linea.substring(15));
								
							}else{
								
								recibirMensaje(linea);
							}

							
						} catch (SocketException se) {
							
							se.printStackTrace();
							
							conectado = false;
							
							try {
								
								socket.close();
								
							} catch (IOException e) {
								
								e.printStackTrace();
							}
							
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					
				}
	
			});
			hiloRecibir.start();
		
		}catch(SocketException se){
			se.printStackTrace();
			desconectar();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}
	
	private void listarClientes(String a) {
		
		this.listClientes.setCellRenderer(new ListCellThing(this.listaIgnorados));
		
		this.modeloLista.removeAllElements();
		
		String[] nicks = a.split("/");
		
		for(String cliente : nicks){
			
			this.modeloLista.addElement(cliente);
			
		}
		
		this.modeloLista.removeElement(this.nick);
		
	}

	public void initialize(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 369, 421);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getScrollPane(), BorderLayout.CENTER);
		contentPane.add(getPanel(), BorderLayout.NORTH);
	}

	public JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.setEnabled(false);
			menuBar.add(getMnArchivo());
		}
		return menuBar;
	}
	public JMenu getMnArchivo() {
		if (mnArchivo == null) {
			mnArchivo = new JMenu("Archivo");
			mnArchivo.setEnabled(false);
			mnArchivo.add(getMntmLogin());
			mnArchivo.add(getMntmDesconectar());
			mnArchivo.add(getMntmLogs());
		}
		return mnArchivo;
	}
	public JMenuItem getMntmLogin() {
		if (mntmLogin == null) {
			mntmLogin = new JMenuItem("Login");
			mntmLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					login();
				}
			});
		}
		return mntmLogin;
	}
	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getListClientes());
		}
		return scrollPane;
	}
	public JList<String> getListClientes() {
		if (listClientes == null) {
			listClientes = new JList<String>();
			listClientes.setBackground(Color.WHITE);
			addPopup(listClientes, getPopupMenu());
			listClientes.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					if(arg0.getClickCount() == 2){
						if(listClientes.getSelectedIndex() != -1){
							
							abrirConversacion(getListClientes().getSelectedValue());
							
						}
							
					}
				}
			});
		}
		return listClientes;
	}
	public JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBackground(Color.LIGHT_GRAY);
			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(getLblNewLabel(), GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
							.addComponent(getLblNombreUsuario(), GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
						.addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(getLblEstadoCliente(), GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(getLblEstadoServidor(), GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
						.addContainerGap())
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addComponent(getLblNewLabel(), GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(getLblNombreUsuario(), GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addComponent(getLblEstadoServidor(), GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(getLblEstadoCliente(), GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap())
			);
			panel.setLayout(gl_panel);
		}
		return panel;
	}
	public JLabel getLblEstadoServidor() {
		if (lblEstadoServidor == null) {
			lblEstadoServidor = new JLabel("Estado Servidor");
			lblEstadoServidor.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));
			lblEstadoServidor.setIcon(new ImageIcon(GuiCliente.class.getResource("/resources/Status-user-busy-icon.png")));
		}
		return lblEstadoServidor;
	}
	public JLabel getLblEstadoCliente() {
		if (lblEstadoCliente == null) {
			lblEstadoCliente = new JLabel("Estado Cliente");
			lblEstadoCliente.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));
			lblEstadoCliente.setIcon(new ImageIcon(GuiCliente.class.getResource("/resources/disconnected.png")));
		}
		return lblEstadoCliente;
	}
	public JMenuItem getMntmDesconectar() {
		if (mntmDesconectar == null) {
			mntmDesconectar = new JMenuItem("Desconectar");
			mntmDesconectar.setEnabled(false);
			mntmDesconectar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					desconectar();
				}
			});
		}
		return mntmDesconectar;
	}
	public JLabel getLblNombreUsuario() {
		if (lblNombreUsuario == null) {
			lblNombreUsuario = new JLabel("Bienvenido a MChat");
			lblNombreUsuario.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));
		}
		return lblNombreUsuario;
	}
	public JPopupMenu getPopupMenu() {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			popupMenu.add(getMntmIgnorar());
		}
		return popupMenu;
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	public JMenuItem getMntmIgnorar() {
		if (mntmIgnorar == null) {
			mntmIgnorar = new JMenuItem("Ignorar");
			mntmIgnorar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if(listClientes.getSelectedIndex() == -1)
						return;
					
					if(listaIgnorados.contains(listClientes.getSelectedValue())){
						listaIgnorados.remove(listClientes.getSelectedValue());
						return;
					}
					
					listaIgnorados.add(listClientes.getSelectedValue());
				}
			});
		}
		return mntmIgnorar;
	}
	public JMenuItem getMntmLogs() {
		if (mntmLogs == null) {
			mntmLogs = new JMenuItem("Logs");
			mntmLogs.setEnabled(false);
			mntmLogs.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					
					GuiLog gl = new GuiLog();
					
					gl.showDialog();
					
				}
			});
		}
		return mntmLogs;
	}
	public JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(GuiCliente.class.getResource("/resources/live-chat.png")));
		}
		return lblNewLabel;
	}
}
