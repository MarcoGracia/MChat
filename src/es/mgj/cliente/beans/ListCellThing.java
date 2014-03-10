package es.mgj.cliente.beans;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListCellThing<String> extends JLabel implements ListCellRenderer<String> {

	private ArrayList<String> listaIgnorados;
    public ListCellThing(ArrayList<String> listaIgnorados) {
    	
        setOpaque(true);
        this.listaIgnorados = listaIgnorados;
        
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        setText(value.toString());
           
        if (listaIgnorados.contains(value.toString())) 
        	
        	this.setForeground(Color.red);
        	
        else
        	
        	this.setForeground(Color.black);
        
        
        if(isSelected)
        	
        	setBackground(Color.cyan);
        
        else
        	setBackground(Color.WHITE);
        
        
        return this;
    }

	
	
}
