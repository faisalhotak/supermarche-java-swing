/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.modele;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author faisalhotak
 */
public class RendererDate extends DefaultTableCellRenderer{
    
    
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column){
        Date date = (Date) value;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE);
        String str = df.format(date);
        this.setText(str);
        this.setHorizontalAlignment(CENTER);
        this.setBackground(null);
        return this;
    }
}
