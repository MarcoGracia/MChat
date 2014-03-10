package es.mgj.cliente.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

import es.mgj.ra2.base.User;
import es.mgj.util.Util;
import es.mgj.util.Util.Accion;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JConecta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfUsuario;
	private JLabel lblNewLabel_1;
	private JPasswordField pfContrasena;
	private JLabel lblNewLabel;
	private JButton btnLogin;
	private JButton btnRegistro;
	private Accion accion;
	private User usuario;

	public JConecta() {

		initialize();
		this.setModal(true);
	}
	
	public Accion mostrarDialogo(){
		this.usuario = new User();
		this.setVisible(true);
		
		return accion;
		
	}
	
	private void cerrar(){
		
		if(!controlCampos())
			return;
		
		this.usuario.setNick(this.getTfUsuario().getText());
		this.usuario.setPassword(String.valueOf(this.pfContrasena.getPassword()));
		
		
		if(Util.db.queryByExample(usuario).hasNext() && this.accion == Accion.REGISTRAR){
			JOptionPane.showMessageDialog(null, 
					"Usuario ya existente, por favor escoja otro nick");
			return;
		}
		
		if(!Util.db.queryByExample(usuario).hasNext() && this.accion == Accion.ACEPTAR){
			JOptionPane.showMessageDialog(null, 
					"Nick o contraseña incorrecta");
			return;
		}
			
		this.setVisible(false);
	}

	private boolean controlCampos() {
		
		if(this.accion == Accion.CANCELAR)
			return true;
		
		if(this.getTfUsuario().getText().length() < 4 ){
			JOptionPane.showMessageDialog(null, 
					"El nombre de usuario debe contener al menos cuatro carácteres");
			return false;
		}
		
		if(this.getTfUsuario().getText().contains("/")){
			JOptionPane.showMessageDialog(null, 
					"El nombre de usuario no puede contener el carácter '/'");
			return false;
		}
		
		if(this.getPfContrasena().getPassword().length < 4){
			JOptionPane.showMessageDialog(null, 
					"La contraseña debe tener al menos cuatro carácteres");
			return false;
		}
		
		
		return true;
		
	}
	
	public User getUser(){
		return this.usuario;
	}
	
	public Accion getAccion(){
		return accion;
	}

	public void initialize(){
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				accion = Accion.CANCELAR;
				cerrar();
			}
		});
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setTitle("Login");
		setBounds(100, 100, 402, 254);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(0, 0, 102));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addContainerGap(64, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(getLblNewLabel())
						.addComponent(getBtnLogin(), GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
						.addComponent(getLblNewLabel_1(), Alignment.LEADING))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(18)
							.addComponent(getBtnRegistro(), GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(41)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(getPfContrasena())
								.addComponent(getTfUsuario(), GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))))
					.addContainerGap(63, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(41)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(getLblNewLabel(), GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(getTfUsuario(), GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(getLblNewLabel_1(), GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(getPfContrasena(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(getBtnRegistro(), GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addComponent(getBtnLogin()))
					.addContainerGap())
		);
		contentPanel.setLayout(gl_contentPanel);
	}
	public JTextField getTfUsuario() {
		if (tfUsuario == null) {
			tfUsuario = new JTextField();
			tfUsuario.setColumns(10);
		}
		return tfUsuario;
	}
	public JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setIcon(new ImageIcon(JConecta.class.getResource("/resources/ContrasenaTexto-141x20.png")));
		}
		return lblNewLabel_1;
	}
	public JPasswordField getPfContrasena() {
		if (pfContrasena == null) {
			pfContrasena = new JPasswordField();
		}
		return pfContrasena;
	}
	public JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(JConecta.class.getResource("/resources/UsuarioTexto-91x30.png")));
		}
		return lblNewLabel;
	}
	public JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton("");
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					accion = Accion.ACEPTAR;
					cerrar();
				}
			});
			btnLogin.setContentAreaFilled(false);
			btnLogin.setBorderPainted(false);
			btnLogin.setBorder(null);
			btnLogin.setIcon(new ImageIcon(JConecta.class.getResource("/resources/LoginBoton50x50.png")));
		}
		return btnLogin;
	}
	public JButton getBtnRegistro() {
		if (btnRegistro == null) {
			btnRegistro = new JButton("");
			btnRegistro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accion = Accion.REGISTRAR;
					cerrar();
				}
			});
			btnRegistro.setIcon(new ImageIcon(JConecta.class.getResource("/resources/RegisterBoton50x50.png")));
			btnRegistro.setContentAreaFilled(false);
			btnRegistro.setBorderPainted(false);
			btnRegistro.setBorder(null);
		}
		return btnRegistro;
	}
}
