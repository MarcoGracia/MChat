package es.mgj.cliente.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import es.mgj.cliente.beans.TablaLogs;
import java.awt.Toolkit;

public class GuiLog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GuiLog dialog = new GuiLog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GuiLog() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(GuiLog.class.getResource("/resources/LoginBoton50x50.png")));
		setTitle("Logs");
		initialize();
		showDialog();
		
	}
	
	public void showDialog() {
		this.setVisible(true);
		
	}

	public void initialize(){
		
		setBounds(100, 100, 624, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				
				TablaLogs tablaLogs = new TablaLogs();
				scrollPane.setViewportView(tablaLogs);
				
			}
		}
	}

	
}
