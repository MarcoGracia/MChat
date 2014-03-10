package es.mgj.cliente.beans;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;

import es.mgj.ra2.base.Log;
import es.mgj.util.Constantes;
import es.mgj.util.Util;

public class TablaLogs extends JTable {
	
	private DefaultTableModel modelo;
	
	public TablaLogs(){
		cabecera();
		listar();
	}

	private void cabecera() {
		
		String[] cabecera = {"Fecha" , "IP" , "Nick" , "Action"};
		
		modelo = new DefaultTableModel(cabecera, 0);
		
		this.modelo.setNumRows(0);
		
		this.setModel(modelo);
		
	}
	
	private void listar(){
		
		
		
		Thread listar = new Thread(new Runnable(){

			@Override
			public void run() {
				
				while(true){
					
					modelo.setNumRows(0);
					
					for(String linea : Log.getLogObject().getLog()){
						
						String [] tempData = linea.split("/");

						if(tempData.length == 5){
							tempData[1] = tempData[1] + tempData[2];
							tempData[2] = tempData[3];
							tempData[3] = tempData[4];
						}
							
						String [] rowData = {tempData[0], tempData[1], tempData[2], tempData[3]};
						
						modelo.addRow(rowData);
					}
					
					try {
						Thread.sleep(10000);
						
						ClientConfiguration configuration = Db4oClientServer.newClientConfiguration();
						configuration.common().updateDepth(2);
						
						Util.db = Db4oClientServer.openClient(configuration, Constantes.HOST, Constantes.PUERTODB4O,
							Constantes.USUARIO, Constantes.CONTRASENA);
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
				
				
			}
			
		});
		
		listar.start();
	}
	
}
