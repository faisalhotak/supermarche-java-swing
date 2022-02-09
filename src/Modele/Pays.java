
package Modele;

import java.sql.Connection;
import Controle.ControleConnexionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class Pays {

    private String nom;
    private static Connection laConnexion = ControleConnexionSingleton.getConnect();
    private final ArrayList<Pays> lesEnreg = new ArrayList<>();

  
    public Pays(String nom) {
        this.nom = nom;
    }

    public Pays() {}

    public ArrayList<Pays> getLesEnreg() {
        lesEnreg.clear();
        lireRecupCRUD();
        return lesEnreg;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT nom FROM pays ORDER BY nom");
            
            while (rs.next()) {
                String nomJ = rs.getString("nom");
                lesEnreg.add(new Pays(nomJ));
            }              
            state.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    } 

    @Override
    public String toString() {
        return this.nom;
    }
}

