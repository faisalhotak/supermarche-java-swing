/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.modele;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author faisalhotak
 */
public class RendererStatutPaiement extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    private final Icon statutOui;
    private final Icon statutNon;
    
    public RendererStatutPaiement(){
        super();
        statutOui = new ImageIcon(getClass().getResource("/Images/Icon_client/statut_on.png"));
        statutNon = new ImageIcon(getClass().getResource("/Images/Icon_client/statut_off.png"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Boolean carte_fidele = (Boolean)value;
        setText("");
        
        if(carte_fidele){
            setIcon(statutOui);
        } else {
            setIcon(statutNon);
        }
        // pour centrer les données de la colonne
        setHorizontalAlignment(CENTER);
        return this;
       
    }
    
    
}
