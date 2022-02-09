package Modele;

import Controle.ControleConnexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class ListeDesUtilisateurs {
        
    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <Utilisateur> listeUtilisateurs;
    
    public ListeDesUtilisateurs(){
        listeUtilisateurs= new ArrayList<>();
        lireRecupCRUD();
    }

    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void setListeUtilisateurs(ArrayList<Utilisateur> listeUtilisateurs) {
        this.listeUtilisateurs = listeUtilisateurs;
    }
    
    public void ajouterUnUtilisateur(Utilisateur U){
        listeUtilisateurs.add(U);
    }
    
    public void retirerUnUtilisateur(int idUtilisateur){
        listeUtilisateurs.remove(idUtilisateur-1);
    }
    
    public boolean estVide(){
        return listeUtilisateurs.size() <= 0;
    }
    
    public ArrayList<Utilisateur> getListeUtilisateursTriee(){
        listeUtilisateurs.clear();
        lireRecupCRUD();
        return listeUtilisateurs;
    }
    
//    public void afficherListeClientsRecherches(String recherche){
//        listeUtilisateurs.clear();
//        chercherCRUD(recherche);
//    }

    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery ("SELECT U.*, E.*, P.* FROM utilisateurs U "
                    + "JOIN Employes E ON E.id = U.id_employe "
                    + "JOIN postes P ON E.id_poste = P.id "
                    + "ORDER BY U.idUtilisateur");
            while (rs.next()) {
                int idJ = rs.getInt("U.idUtilisateur");
                String nomUtilisateurJ = rs.getString("U.nomUtilisateur");
                String mdpUtilisateurJ = rs.getString("U.mdpUtilisateur");
                int droitsJ = rs.getInt("U.droits");

                int idE = rs.getInt("E.id");
                String nomE = rs.getString("E.nom");
                String prenomE = rs.getString("E.prenom"); 
                int idPosteE = rs.getInt("E.id");
                String posteE = rs.getString("P.designation");
                String adresseE = rs.getString("E.rue");
                String code_postalE = rs.getString("E.code_postal");
                String localiteE = rs.getString("E.localite");
                String paysE = rs.getString("E.pays");
                String tel_fixeE = rs.getString("E.num_telephone");
                String mobileE = rs.getString("E.num_mobile");
                double salaireE = Double.parseDouble(rs.getString("E.salaire"));
                String emailE = rs.getString("E.email");
                Date date_creationE = rs.getDate("E.date_embauche");
                
                // Création du poste
                Poste lePoste = new Poste(idPosteE, posteE);
                
                // Création de l'employé à l'aide des informations reçues
                Employe emp = new Employe(idE, nomE, prenomE, adresseE, 
                        code_postalE, localiteE, paysE, tel_fixeE, mobileE, 
                        emailE, date_creationE, lePoste, salaireE);
                
                // Création de l'utilisateur à l'aide des informations reçues
                Utilisateur recupUtilisateur = new Utilisateur(idJ, nomUtilisateurJ, mdpUtilisateurJ,
                        droitsJ, emp);
                
                // Ajout de l'utilisateur vers la liste des utilisateurs
                ajouterUnUtilisateur(recupUtilisateur);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Utilisateur lireRecupEnreg(Utilisateur util){
        try {
            Statement state = laConnexion.createStatement();
                        ResultSet rs = state.executeQuery ("SELECT U.*, E.*, P.* FROM utilisateurs U "                               
                    + "JOIN Employes E ON E.id = U.id_employe "
                    + "JOIN postes P ON E.id_poste = P.id "
                    + "ORDER BY U.idUtilisateur DESC LIMIT 1");
            if(rs != null)
            {
                rs.next();
                util.setId(rs.getInt("U.idUtilisateur"));
                util.setNomUtilisateur(rs.getString("U.nomUtilisateur"));
                util.setMdp(rs.getString("U.mdpUtilisateur"));
                util.setDroits(rs.getInt("U.droits"));
                
                int idE = rs.getInt("E.id");
                String nomE = rs.getString("E.nom");
                String prenomE = rs.getString("E.prenom");
                int idPosteE = rs.getInt("P.id");
                String posteE = rs.getString("P.designation");
                String adresseE = rs.getString("E.rue");
                String code_postalE = rs.getString("E.code_postal");
                String localiteE = rs.getString("E.localite");
                String paysE = rs.getString("E.pays");
                String tel_fixeE = rs.getString("E.num_telephone");
                String mobileE = rs.getString("E.num_mobile");
                double salaireE = Double.parseDouble(rs.getString("E.salaire"));
                String emailE = rs.getString("E.email");
                Date date_creationE = rs.getDate("E.date_embauche");
                
                // Création du poste
                Poste lePoste = new Poste(idPosteE, posteE);
                
                // Création de l'employé à l'aide des informations reçues
                Employe emp = new Employe(idE, nomE, prenomE, adresseE, 
                        code_postalE, localiteE, paysE, tel_fixeE, mobileE, 
                        emailE, date_creationE, lePoste, salaireE);
                
                util.setlEmploye(emp);
                               
                // Création de l'utilisateur à l'aide des informations reçues
                Utilisateur recupEnregUtilisateur = new Utilisateur(util.getId(), util.getNomUtilisateur(), util.getMdp(),
                        util.getDroits(), util.getlEmploye());

                // Ajout de l'utilisateur vers la liste des utilisateurs
                ajouterUnUtilisateur(recupEnregUtilisateur); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return util;
    }
    
    public boolean creerCRUD(Utilisateur util) {
        boolean bCreation = false;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "utilisateurs (nomUtilisateur, mdpUtilisateur, droits, id_employe) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setString(1, util.getNomUtilisateur());
            prepare.setString(2, util.getMdp());
            prepare.setInt(3, util.getDroits());
            prepare.setInt(4, util.getlEmploye().getCode());

            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
            
            // Récupération de l'enregistrement et ajout dans la liste des utilisateurs
            lireRecupEnreg(util);
            
            bCreation = true;
            
            // Fermetrue de la transaction
            laConnexion.setAutoCommit(true);          
        } 
        catch (SQLException e) 
        {           
            try {
                laConnexion.setAutoCommit(true);
            } catch (SQLException sqlE){};
            
            if (e.getErrorCode() == 1062) {
                if(e.getMessage().contains("nomUtilisateur")) {
                    JOptionPane.showMessageDialog(null, "Ce nom d'utilisateur existe déjà !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                }
                if(e.getMessage().contains("id_employe")){
                    JOptionPane.showMessageDialog(null, "Cet employé possède déjà des identifiants !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                }               
            } else {                           
                JOptionPane.showMessageDialog(null,
                        "Ajout dans la BD non effectué : "
                        + e.getMessage(), "Problème rencontré",
                        JOptionPane.ERROR_MESSAGE);      
            }
           
            
            //laConnexion.rollback();// c'est ici que l'on annule la transaction 

            
        }   
        /* finally {
            valueLock.unlock();
        */
        return bCreation;
    }
    
    public boolean modifierCRUD(Utilisateur util) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE utilisateurs SET"
                    + " nomUtilisateur = ?,"
                    + " mdpUtilisateur = ?,"
                    + " droits = ?,"
                    + " id_employe = ?"
                    + " WHERE idUtilisateur = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, util.getNomUtilisateur());
            prepare.setString(2, util.getMdp());
            prepare.setInt(3, util.getDroits());
            prepare.setInt(4, util.getlEmploye().getCode()); 
            prepare.setInt(5, util.getId());
            prepare.executeUpdate();
            prepare.close();
            bModification = true;
            
            // Fermeture de la transaction
            laConnexion.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                laConnexion.setAutoCommit(true);
            } catch (SQLException sqlE){};
            
            if (e.getErrorCode() == 1062) {
                if(e.getMessage().contains("nomUtilisateur")) {
                    JOptionPane.showMessageDialog(null, "Ce nom d'utilisateur existe déjà !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                }
                if(e.getMessage().contains("id_employe")){
                    JOptionPane.showMessageDialog(null, "Cet employé possède déjà des identifiants !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                }               
            } else {                           
                JOptionPane.showMessageDialog(null,
                        "Ajout dans la BD non effectué : "
                        + e.getMessage(), "Problème rencontré",
                        JOptionPane.ERROR_MESSAGE);      
            }
        }
        return bModification;
    }

//    public boolean supprimerCRUD(String vCode){
//        boolean bSuppression = false;
//        int carte;
//        String requete = null;
//        try {
//            // Ouverture d'une transaction
//            laConnexion.setAutoCommit(false);
//            
//            requete = "UPDATE clients SET"
//                    + " nom = ?,"
//                    + " prenom = ?,"
//                    + " rue = ?,"
//                    + " code_postal = ?,"
//                    + " localite = ?,"
//                    + " pays = ?,"
//                    + " num_telephone = ?,"
//                    + " num_mobile = ?,"
//                    + " email = ?,"
//                    + " est_supprime = 1"
//                    + " WHERE id = ?";
//            
//            PreparedStatement prepare = laConnexion.prepareStatement(requete);
//            
//            prepare.setString(1, "Client supprimé n°" + vCode);
//            prepare.setString(2, "");
//            prepare.setString(3, "");
//            prepare.setString(4, "");
//            prepare.setString(5, "");
//            prepare.setString(6, "");
//            prepare.setString(7, "");
//            prepare.setString(8, "");
//            prepare.setString(9, "");
//            prepare.setInt(10, Integer.parseInt(vCode));
//            prepare.executeUpdate();
//            prepare.close();
//            bSuppression = true;
//            
//            // Fermeture de la transaction
//            laConnexion.setAutoCommit(true);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Modif dans la BD non effectuée : "
//                    + e.getMessage(), "Problème rencontré",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//        return bSuppression;
//    }
    
    public boolean supprimerCRUD(String vCode) {
        boolean bSuppression = false;
        String requete = null;
        
        try {

            requete = "DELETE FROM utilisateurs WHERE idUtilisateur = '" + vCode + "'";
            Statement state3 = laConnexion.createStatement();
            int nbEnregSup = state3.executeUpdate(requete);
            state3.close();

            // Si le nombre d'enregistrements est égal à 0
            if (nbEnregSup == 0) {
                JOptionPane.showMessageDialog(null,
                        "Aucun enregistrement correspondant.",
                        "Résultat", JOptionPane.ERROR_MESSAGE);
            }
            else bSuppression = true;
        } catch (SQLException e) {
            bSuppression = false;
            JOptionPane.showMessageDialog(null,
                    "Aucune suppression effectuée dans la BD : "
                    + e.getMessage(),
                    "Problème rencontré", JOptionPane.ERROR_MESSAGE);
        }
        return bSuppression;
    } 
    
    public Utilisateur chercherCRUD(String nomUser, String mdp) { 
        Utilisateur chercherUtil = null;
        String requete = "SELECT U.*, E.*, P.* FROM utilisateurs U"
                + " JOIN Employes E ON E.id = U.id_employe"
                + " JOIN postes P ON E.id_poste = P.id"
                + " WHERE nomUtilisateur = '" + nomUser + "'"
                + " AND mdpUtilisateur = '" + mdp + "'"
                + " LIMIT 1";
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery(requete);
            while (rs.next()) {
                int idJ = rs.getInt("U.idUtilisateur");
                String nomUtilisateurJ = rs.getString("U.nomUtilisateur");
                String mdpUtilisateurJ = rs.getString("U.mdpUtilisateur");
                int droitsJ = rs.getInt("U.droits");
                
                int idE = rs.getInt("E.id");
                String nomE = rs.getString("E.nom");
                String prenomE = rs.getString("E.prenom");
                int idPosteE = rs.getInt("P.id");
                String posteE = rs.getString("P.designation");
                String adresseE = rs.getString("E.rue");
                String code_postalE = rs.getString("E.code_postal");
                String localiteE = rs.getString("E.localite");
                String paysE = rs.getString("E.pays");
                String tel_fixeE = rs.getString("E.num_telephone");
                String mobileE = rs.getString("E.num_mobile");
                double salaireE = Double.parseDouble(rs.getString("E.salaire"));
                String emailE = rs.getString("E.email");
                Date date_creationE = rs.getDate("E.date_embauche");
                
                // Création du poste
                Poste lePoste = new Poste(idPosteE, posteE);
                
                // Création de l'employé à l'aide des informations reçues
                Employe emp = new Employe(idE, nomE, prenomE, adresseE, 
                        code_postalE, localiteE, paysE, tel_fixeE, mobileE, 
                        emailE, date_creationE, lePoste, salaireE);

                chercherUtil = new Utilisateur(idJ, nomUtilisateurJ, mdpUtilisateurJ,
                                    droitsJ, emp);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : "
                    + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return chercherUtil;
    }
}