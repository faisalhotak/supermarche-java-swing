/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.modele;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author faisalhotak
 */
public class RendererPrixMontantRestant extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) 
    {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (Double.parseDouble(String.valueOf(value)) > 0) setForeground(Color.RED);
        else setForeground(Color.GREEN);
        setValue(value + " €");
        setHorizontalAlignment(RIGHT);
        setFont(getFont().deriveFont(Font.BOLD));
        return this;
    }
}
