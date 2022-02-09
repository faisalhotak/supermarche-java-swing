package Modele;

import Controle.ControleConnexionSingleton;
import Controle.utili.GestionsDates;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author faisalhotak
 */

public class ListeDesEmployes {
        
    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <Employe> listeEmployes;
    
    public ListeDesEmployes(){
        listeEmployes = new ArrayList<>();
        lireRecupCRUD();
    }
    
    public void ajouterUnEmploye(Employe E){       
        listeEmployes.add(E);
    }
    
    public void remplacerUnEmploye(int idAncienEmploye, Employe nouveau){      
        listeEmployes.set(idAncienEmploye, nouveau);
    }
    
    public void retirerUnEmploye(int idEmploye){
        listeEmployes.remove(idEmploye-1);
    }
    
    public boolean estVide(){
        return listeEmployes.size() <= 0;
    } 

    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<Employe> getListeEmployes() {
        return listeEmployes;
    }

    public void setListeEmployes(ArrayList<Employe> listeEmployes) {
        this.listeEmployes = listeEmployes;
    }
    
    public ArrayList<Employe> getListeEmployesTriee(){
        listeEmployes.clear();
        lireRecupCRUD();
        return listeEmployes;
    }
    
    public void afficherListeEmployesRecherches(String recherche){
        listeEmployes.clear();
        chercherCRUD(recherche);
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT employes.id, nom, prenom, "
                    + "postes.id, postes.designation, rue, code_postal, localite, "
                    + "pays, num_telephone, num_mobile, salaire, "
                    + "email, date_embauche from employes, postes "
                    + "WHERE employes.id_poste = postes.id "
                    + "AND est_supprime = 0 ORDER BY nom");
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String prenomJ = rs.getString("prenom");
                int idPosteJ = rs.getInt("postes.id");
                String posteJ = rs.getString("postes.designation");
                String adresseJ = rs.getString("rue");
                String code_postalJ = rs.getString("code_postal");
                String localiteJ = rs.getString("localite");
                String paysJ = rs.getString("pays");
                String tel_fixeJ = rs.getString("num_telephone");
                String mobileJ = rs.getString("num_mobile");
                double salaireJ = Double.parseDouble(rs.getString("salaire"));
                String emailJ = rs.getString("email");
                Date date_creationJ = rs.getDate("date_embauche");
                
                // Création du poste
                Poste lePoste = new Poste(idPosteJ, posteJ);
                
                // Création de l'employé à l'aide des informations reçues
                Employe recupEmploye = new Employe(idJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, lePoste, salaireJ);
                
                // Ajout de l'employé vers la liste des employés
                ajouterUnEmploye(recupEmploye);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Employe lireRecupEnreg(Employe emp){
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT employes.id, nom, prenom, "
                    + "postes.id, postes.designation, rue, code_postal, localite, "
                    + "pays, num_telephone, num_mobile, salaire, "
                    + "email, date_embauche from employes, postes "
                    + "WHERE employes.id_poste = postes.id ORDER BY employes.id "
                    + "DESC LIMIT 1");
            if(rs != null)
            {
                rs.next();
                emp.setCode(rs.getInt("id"));
                emp.setNom(rs.getString("nom"));
                emp.setPrenom(rs.getString("prenom"));
                int idPosteJ = rs.getInt("postes.id");
                String posteJ = (rs.getString("postes.designation"));
                emp.setAdresse(new Adresse(rs.getString("rue"), rs.getString("code_postal"), rs.getString("localite"), rs.getString("pays")));
                emp.setNum_telephone(rs.getString("num_telephone"));
                emp.setNum_mobile(rs.getString("num_mobile"));
                emp.setSalaire(Double.parseDouble(rs.getString("salaire")));
                emp.setEmail(rs.getString("email"));
                emp.setDate_embauche(rs.getDate("date_embauche"));
                
                // Création du poste
                Poste lePoste = new Poste(idPosteJ, posteJ);
                emp.setPoste(lePoste);
                
                // Création de l'employé à l'aide des informations reçues
                Employe recupEnregEmploye = new Employe(emp.getCode(), emp.getNom(),
                emp.getPrenom(), emp.getAdresse().getRue(), emp.getAdresse().getCode_postal(), 
                emp.getAdresse().getLocalite(), emp.getAdresse().getPays(),
                emp.getNum_telephone(), emp.getNum_mobile(), emp.getEmail(),
                emp.getDate_embauche(), emp.getPoste(), emp.getSalaire());

                // Ajout de l'employé vers la liste des employés
                ajouterUnEmploye(recupEnregEmploye); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return emp;
    }
    
    public boolean creerCRUD(Employe emp) {
        boolean bCreation = false;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "employes (nom, prenom, id_poste, rue, code_postal, "
                    + "localite, pays, num_telephone, num_mobile, salaire, "
                    + "email, date_embauche)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setString(1, emp.getNom());
            prepare.setString(2, emp.getPrenom());
            prepare.setInt(3, emp.getPoste().getId());
            prepare.setString(4, emp.getAdresse().getRue());
            prepare.setString(5, emp.getAdresse().getCode_postal());
            prepare.setString(6, emp.getAdresse().getLocalite());
            prepare.setString(7, emp.getAdresse().getPays());
            prepare.setString(8, emp.getNum_telephone());
            prepare.setString(9, emp.getNum_mobile());
            prepare.setDouble(10, emp.getSalaire());
            prepare.setString(11, emp.getEmail());
            prepare.setString(12, String.valueOf(GestionsDates.dateJavaEnDateSQL(emp.getDate_embauche())));

            
            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
            
            // Récupération de l'enregistrement et ajout dans la liste des clients
            lireRecupEnreg(emp);
            
            bCreation = true;
            
            // Fermetrue de la transaction
            laConnexion.setAutoCommit(true);          
        } 
        catch (SQLException e) 
        {
            //laConnexion.rollback();// c'est ici que l'on annule la transaction 
            //laConnexion.setAutoCommit(true); //fermeture de la transaction
            JOptionPane.showMessageDialog(null,
                    "Ajout dans la BD non effectué : "
                    + e.getMessage(), "Problème rencontré",
                    JOptionPane.ERROR_MESSAGE);
        }   
        /* finally {
            valueLock.unlock();
        */
        return bCreation;
    }
    
    public boolean modifierCRUD(Employe emp) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE employes SET"
                    + " nom = ?,"
                    + " prenom = ?,"
                    + " id_poste = ?,"
                    + " rue = ?,"
                    + " code_postal = ?,"
                    + " localite = ?,"
                    + " pays = ?,"
                    + " num_telephone = ?,"
                    + " num_mobile = ?,"
                    + " salaire = ?,"
                    + " email = ?,"
                    + " date_embauche = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, emp.getNom());
            prepare.setString(2, emp.getPrenom());
            prepare.setInt(3, emp.getPoste().getId());
            prepare.setString(4, emp.getAdresse().getRue());
            prepare.setString(5, emp.getAdresse().getCode_postal());
            prepare.setString(6, emp.getAdresse().getLocalite());
            prepare.setString(7, emp.getAdresse().getPays());
            prepare.setString(8, emp.getNum_telephone());
            prepare.setString(9, emp.getNum_mobile());
            prepare.setDouble(10, emp.getSalaire());
            prepare.setString(11, emp.getEmail());
            prepare.setString(12, String.valueOf(GestionsDates.dateJavaEnDateSQL(emp.getDate_embauche())));          
            prepare.setInt(13, emp.getCode());
            
            prepare.executeUpdate();
            prepare.close();
            bModification = true;
            
            // Fermeture de la transaction
            laConnexion.setAutoCommit(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Modif dans la BD à la table 'employes' non effectuée : "
                    + e.getMessage(), "Problème rencontré",
                    JOptionPane.ERROR_MESSAGE);
        }
        return bModification;
    }

//    public boolean supprimerCRUD(String vCode) {
//        boolean bSuppression = false;
//        String requete = null;
//        
//        try {
//            // On supprime d'abord la FK dans les Lignes Ticket Caisse
//            requete = "DELETE FROM lignes_ticket_caisse WHERE id_ticket_caisse = ANY"
//                    + "(SELECT id FROM tickets_caisse WHERE id_employe = '" + vCode + "')";            
//            Statement state = laConnexion.createStatement();
//            state.executeUpdate(requete);
//            state.close();
//            
//            // Puis on supprime la FK dans Tickets de Caisse
//            requete = "DELETE FROM tickets_caisse WHERE id_employe = '" + vCode + "'";
//            Statement state2 = laConnexion.createStatement();
//            state2.executeUpdate(requete);
//            state2.close();
//            
//            // On supprime finalement la ligne de la table Employes
//            requete = "DELETE FROM employes WHERE id = '" + vCode + "'";
//            Statement state3 = laConnexion.createStatement();
//            int nbEnregSup = state3.executeUpdate(requete);
//            state3.close();
//
//            // Si le nombre d'enregistrements est égal à 0
//            if (nbEnregSup == 0) {
//                JOptionPane.showMessageDialog(null,
//                        "Aucun enregistrement correspondant.",
//                        "Résultat", JOptionPane.ERROR_MESSAGE);
//            }
//            else bSuppression = true;
//        } catch (SQLException e) {
//            bSuppression = false;
//            JOptionPane.showMessageDialog(null,
//                    "Aucune suppression effectuée dans la BD : "
//                    + e.getMessage(),
//                    "Problème rencontré", JOptionPane.ERROR_MESSAGE);
//        }
//        return bSuppression;
//    }
    
    public boolean supprimerCRUD(String vCode){
        boolean bSuppression = false;
        int carte;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE employes SET"
                    + " nom = ?,"
                    + " prenom = ?,"
                    + " rue = ?,"
                    + " code_postal = ?,"
                    + " localite = ?,"
                    + " pays = ?,"
                    + " num_telephone = ?,"
                    + " num_mobile = ?,"
                    + " salaire = ?,"
                    + " email = ?,"
                    + " est_supprime = 1"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, "Employé supprimé n°" + vCode);
            prepare.setString(2, "");
            prepare.setString(3, "");
            prepare.setString(4, "");
            prepare.setString(5, "");
            prepare.setString(6, "");
            prepare.setString(7, "");
            prepare.setString(8, "");
            prepare.setDouble(9, 0.0);
            prepare.setString(10, "");
            prepare.setInt(11, Integer.parseInt(vCode));
            prepare.executeUpdate();
            prepare.close();
            bSuppression = true;
            
            // Fermeture de la transaction
            laConnexion.setAutoCommit(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Modif dans la BD non effectuée : "
                    + e.getMessage(), "Problème rencontré",
                    JOptionPane.ERROR_MESSAGE);
        }
        return bSuppression;
    }
    
    public ArrayList<Employe> chercherCRUD(String recherche) {
        
//        Statement state = laConnexion.createStatement();
//        ResultSet rs = state.executeQuery("SELECT employes.id, nom, prenom, "
//                + "designation, rue, code_postal, localite, "
//                + "pays, num_telephone, num_mobile, salaire, "
//                + "email, date_embauche from employes, postes "
//                + "WHERE employes.id_poste = postes.id ORDER BY nom");
                                   
//        String requete2 = "SELECT * FROM clients"
//                + " WHERE nom LIKE '%" + recherche + "%'"
//                + " OR prenom LIKE '%" + recherche + "%'";
       
        String requete = "SELECT employes.id, nom, prenom, "
                + "postes.id, postes.designation, rue, code_postal, localite, "
                + "pays, num_telephone, num_mobile, salaire, "
                + "email, date_embauche from employes, postes "
                + "WHERE employes.id_poste = postes.id "
                + "AND (employes.nom LIKE '%" + recherche + "%' "
                + "OR employes.prenom LIKE '%" + recherche + "%') "
                + "ORDER BY nom";
               
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery(requete);
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String prenomJ = rs.getString("prenom");
                int idPosteJ = rs.getInt("postes.id");
                String posteJ = rs.getString("postes.designation");
                String adresseJ = rs.getString("rue");
                String code_postalJ = rs.getString("code_postal");
                String localiteJ = rs.getString("localite");
                String paysJ = rs.getString("pays");
                String tel_fixeJ = rs.getString("num_telephone");
                String mobileJ = rs.getString("num_mobile");
                double salaireJ = Double.parseDouble(rs.getString("salaire"));
                String emailJ = rs.getString("email");
                Date date_creationJ = rs.getDate("date_embauche");

                // Création du poste
                Poste lePoste = new Poste(idPosteJ, posteJ);
                
                // Création de l'employé à l'aide des informations reçues
                Employe recupEmploye = new Employe(idJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, lePoste, salaireJ);

                // Ajout de l'employé vers la liste des employés
                ajouterUnEmploye(recupEmploye);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : "
                    + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return listeEmployes;
    }
}