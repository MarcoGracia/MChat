package es.mgj.cliente.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Toolkit;
import java.awt.Color;

public class GuiConversacion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String nickReceptor;
	private String nickEmisor;
	private JTextField tfTextoMandado;
	private JScrollPane scrollPane;
	private JButton btEmoticonoSmile;
	private JTextPane tpTextoCompleto;
	private JButton btnEmoticonoZombie;
	private JButton btnEmoticonoAngry;
	private JButton btnEmoticonoSigh;
	private JButton btnEmoticonoCry;
	private JButton btnEmoticonoBadass;
	private JButton btnEmoticonoHurt;
	private GuiCliente guiCliente;
	private JLabel lblEscribiendo;
	
	public GuiConversacion(GuiCliente guiCliente) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GuiConversacion.class.getResource("/resources/iChat-Emoticon.png")));
		this.guiCliente = guiCliente;
		
		initialize();
		
	}
	
	public void mostrarConversacion(String nick, String nickReceptor) {
		
		this.nickEmisor = nick;
		this.nickReceptor = nickReceptor;
		this.setTitle("Conversacion con " + this.nickReceptor);
		this.tfTextoMandado.requestFocus();
		this.setVisible(true);
		
	}
	
	public void ocultarConversacion() {
		this.setVisible(false);
		
	}
	
	
	public void anadirLinea(String emisor, String mensaje){
		
		if(emisor.equals("Servidor")){
			
			this.tfTextoMandado.setEnabled(false);
		}else{
			this.tfTextoMandado.setEnabled(true);
		}
		
		if(mensaje.contains("/t/tecleando")){
			this.lblEscribiendo.setText( this.nickEmisor + " está escribiendo...");
			return;
		}
		if(mensaje.contains("/tf/tecleando")){
			this.lblEscribiendo.setText("");
			return;
		}
			
			
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		StyleConstants.setBold(attrs, true);
		 
		 try {
			 tpTextoCompleto.getStyledDocument().insertString(
						tpTextoCompleto.getStyledDocument().getLength(), 
						emisor + ": ", 
						attrs);
			 
			 String[] mensajeSplit = mensaje.split("\\|");
			 
			 for(String m : mensajeSplit){
				 switch (m){
					case ":)":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-smile.png")));
						
						break;
					case ">:O.":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-zombie.png")));
						
						break;
					case ">:(":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-angry.png")));
						
						break;
					case ":/":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-sigh.png")));
						
						break;
					case ":'(":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-cry.png")));
						
						break;
					case ":-'":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-badass.png")));
						
						break;
					case "=:o]":
						tpTextoCompleto.setCaretPosition(this.tpTextoCompleto.getStyledDocument().getLength());
						tpTextoCompleto.insertIcon(
								new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-hurt.png")));
						
						break;
					default:
						tpTextoCompleto.getStyledDocument().insertString(
								tpTextoCompleto.getStyledDocument().getLength(), 
								m, 
								null);
						
						
				}
				
			 }
			
			tpTextoCompleto.getStyledDocument().insertString(
							tpTextoCompleto.getStyledDocument().getLength(), 
							"\n", 
							null);
					
			} catch (BadLocationException e) {
				
				e.printStackTrace();
			}
		 
		 this.tfTextoMandado.requestFocus();
	}
	
	protected void anadirEmoticono(int index) {
		
		switch (index){
			case 0:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|:)|");
				break;
			case 1:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|>:O.|");
				break;
			case 2:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|>:(|");
				break;
			case 3:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|:/|");
				break;
			case 4:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|:'(|");
				break;
			case 5:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|:-'|");
				break;
			case 6:
				tfTextoMandado.setText(tfTextoMandado.getText() + "|=:o]|");
				break;
			}
			
			this.tfTextoMandado.requestFocus();
		
	}
	
	public void inicializar(){
		
	}
	
	public void initialize(){
		setResizable(false);
		setBounds(100, 100, 459, 302);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			tfTextoMandado = new JTextField();
			tfTextoMandado.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					
					if(tfTextoMandado.getText().length() == 0){
						guiCliente.enviarMensaje("/tf/tecleando", nickReceptor);
						return;
					}
						
					if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
						
						anadirLinea(nickEmisor, tfTextoMandado.getText());
						
						guiCliente.enviarMensaje("/tf/tecleando", nickReceptor);
						
						guiCliente.enviarMensaje(tfTextoMandado.getText(), nickReceptor);
						
						tfTextoMandado.setText("");
					}else
						guiCliente.enviarMensaje("/t/tecleando", nickReceptor);
				}
			});
			contentPanel.add(tfTextoMandado, BorderLayout.SOUTH);
			tfTextoMandado.setColumns(10);
		}
		{
			JPanel panel = new JPanel();
			panel.setBackground(Color.GRAY);
			contentPanel.add(panel, BorderLayout.CENTER);
			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(getBtEmoticonoSmile(), GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(getBtnEmoticonoZombie())
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(getBtnEmoticonoAngry(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(getBtnEmoticonoSigh(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(getBtnEmoticonoCry(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(getBtnEmoticonoBadass(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(getBtnEmoticonoHurt(), GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addGap(205))
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(getLblEscribiendo(), GroupLayout.PREFERRED_SIZE, 415, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(28, Short.MAX_VALUE))
					.addGroup(gl_panel.createSequentialGroup()
						.addComponent(getScrollPane(), GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
						.addContainerGap())
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(6)
						.addComponent(getLblEscribiendo(), GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(getScrollPane(), GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addComponent(getBtEmoticonoSmile())
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(getBtnEmoticonoZombie())
								.addComponent(getBtnEmoticonoAngry(), GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(getBtnEmoticonoSigh(), GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(getBtnEmoticonoCry(), GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(getBtnEmoticonoBadass(), GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addComponent(getBtnEmoticonoHurt(), GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap())
			);
			panel.setLayout(gl_panel);
		}
	}

	public String getNickReceptor() {
		return nickReceptor;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTpTextoCompleto());
		}
		return scrollPane;
	}
	public JButton getBtEmoticonoSmile() {
		if (btEmoticonoSmile == null) {
			btEmoticonoSmile = new JButton("");
			btEmoticonoSmile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					anadirEmoticono(0);
					
				}
			});
			btEmoticonoSmile.setContentAreaFilled(false);
			btEmoticonoSmile.setBorder(null);
			btEmoticonoSmile.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-smile.png")));
		}
		return btEmoticonoSmile;
	}
	

	public JTextPane getTpTextoCompleto() {
		if (tpTextoCompleto == null) {
			tpTextoCompleto = new JTextPane();
			tpTextoCompleto.setEditable(false);
		}
		return tpTextoCompleto;
	}
	public JButton getBtnEmoticonoZombie() {
		if (btnEmoticonoZombie == null) {
			btnEmoticonoZombie = new JButton("");
			btnEmoticonoZombie.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(1);
				}
			});
			btnEmoticonoZombie.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-zombie.png")));
			btnEmoticonoZombie.setDefaultCapable(false);
			btnEmoticonoZombie.setContentAreaFilled(false);
			btnEmoticonoZombie.setBorderPainted(false);
			btnEmoticonoZombie.setBorder(null);
		}
		return btnEmoticonoZombie;
	}
	public JButton getBtnEmoticonoAngry() {
		if (btnEmoticonoAngry == null) {
			btnEmoticonoAngry = new JButton("");
			btnEmoticonoAngry.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(2);
				}
			});
			btnEmoticonoAngry.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-angry.png")));
			btnEmoticonoAngry.setDefaultCapable(false);
			btnEmoticonoAngry.setContentAreaFilled(false);
			btnEmoticonoAngry.setBorderPainted(false);
			btnEmoticonoAngry.setBorder(null);
		}
		return btnEmoticonoAngry;
	}
	public JButton getBtnEmoticonoSigh() {
		if (btnEmoticonoSigh == null) {
			btnEmoticonoSigh = new JButton("");
			btnEmoticonoSigh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(3);
				}
			});
			btnEmoticonoSigh.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-sigh.png")));
			btnEmoticonoSigh.setDefaultCapable(false);
			btnEmoticonoSigh.setContentAreaFilled(false);
			btnEmoticonoSigh.setBorderPainted(false);
			btnEmoticonoSigh.setBorder(null);
		}
		return btnEmoticonoSigh;
	}
	public JButton getBtnEmoticonoCry() {
		if (btnEmoticonoCry == null) {
			btnEmoticonoCry = new JButton("");
			btnEmoticonoCry.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(4);
				}
			});
			btnEmoticonoCry.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-cry.png")));
			btnEmoticonoCry.setDefaultCapable(false);
			btnEmoticonoCry.setContentAreaFilled(false);
			btnEmoticonoCry.setBorderPainted(false);
			btnEmoticonoCry.setBorder(null);
		}
		return btnEmoticonoCry;
	}
	public JButton getBtnEmoticonoBadass() {
		if (btnEmoticonoBadass == null) {
			btnEmoticonoBadass = new JButton("");
			btnEmoticonoBadass.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(5);
				}
			});
			btnEmoticonoBadass.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-badass.png")));
			btnEmoticonoBadass.setDefaultCapable(false);
			btnEmoticonoBadass.setContentAreaFilled(false);
			btnEmoticonoBadass.setBorderPainted(false);
			btnEmoticonoBadass.setBorder(null);
		}
		return btnEmoticonoBadass;
	}
	public JButton getBtnEmoticonoHurt() {
		if (btnEmoticonoHurt == null) {
			btnEmoticonoHurt = new JButton("");
			btnEmoticonoHurt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					anadirEmoticono(6);
				}
			});
			btnEmoticonoHurt.setIcon(new ImageIcon(GuiConversacion.class.getResource("/resources/emoticono-hurt.png")));
			btnEmoticonoHurt.setDefaultCapable(false);
			btnEmoticonoHurt.setContentAreaFilled(false);
			btnEmoticonoHurt.setBorderPainted(false);
			btnEmoticonoHurt.setBorder(null);
		}
		return btnEmoticonoHurt;
	}
	
	public String getNickEmisor() {
		return nickEmisor;
	}

	public void setNickEmisor(String nickEmisor) {
		this.nickEmisor = nickEmisor;
	}

	public void setNickReceptor(String nickReceptor) {
		this.nickReceptor = nickReceptor;
	}
	public JLabel getLblEscribiendo() {
		if (lblEscribiendo == null) {
			lblEscribiendo = new JLabel("");
		}
		return lblEscribiendo;
	}
}
