
package Modele;

import java.sql.Connection;
import Controle.ControleConnexionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class ModePaiement {

    private int id;
    private String mode;
    private Connection laConnexion = ControleConnexionSingleton.getConnect();
    private final ArrayList<ModePaiement> lesEnreg = new ArrayList<>();

  
    public ModePaiement(int id, String mode) {
        this.id = id;
        this.mode = mode;
    }

    // Constructeur vide
    public ModePaiement() {}

    public ArrayList<ModePaiement> getLesEnreg() {
        lesEnreg.clear();
        lireRecupCRUD();
        return lesEnreg;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM modes_paiement ORDER BY id");
            
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String modeJ = rs.getString("mode");
                lesEnreg.add(new ModePaiement(idJ, modeJ));
            }                
            state.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré dans la table 'modes' : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    } 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.mode;
    }
}

