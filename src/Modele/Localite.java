
package Modele;

import java.sql.Connection;
import Controle.ControleConnexionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class Localite {

    private String nom, codePostal;
    private static Connection laConnexion = ControleConnexionSingleton.getConnect();
    private final ArrayList<Localite> lesEnreg = new ArrayList<>();

  
    public Localite(String nom, String codePostal) {
        this.nom = nom;
        this.codePostal = codePostal;
    }

    public Localite() {}

    public ArrayList<Localite> getLesEnreg() {
        lesEnreg.clear();
        lireRecupCRUD();
        return lesEnreg;
    }

    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT nom, codePostal FROM localite ORDER BY nom");
            
            while (rs.next()) {
                String nomLocaliteJ = rs.getString("nom");
                String codePostalJ = rs.getString("codePostal");
               
                
                lesEnreg.add(new Localite(nomLocaliteJ, codePostalJ));
            }                
            state.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    } 

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @Override
    public String toString() {
        return this.nom + "     " + this.codePostal;
    }
}

