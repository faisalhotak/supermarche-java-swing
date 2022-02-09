
package Modele;

import java.sql.Connection;
import Controle.ControleConnexionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class Poste {

    private int id;
    private String designation;
    private Connection laConnexion = ControleConnexionSingleton.getConnect();
    private final ArrayList<Poste> lesEnreg = new ArrayList<>();

  
    public Poste(int id, String designation) {
        this.id = id;
        this.designation = designation;
    }

    // Constructeur vide
    public Poste() {}

    public ArrayList<Poste> getLesEnreg() {
        lesEnreg.clear();
        lireRecupCRUD();
        return lesEnreg;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM postes ORDER BY id");
            
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String designationJ = rs.getString("designation");
                lesEnreg.add(new Poste(idJ, designationJ));
            }                
            state.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré dans la table 'postes' : " + e.getMessage(),
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
        return this.designation;
    }
}

