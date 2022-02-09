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

public class ListeDesClients {
        
    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <Client> listeClients;
    
    public ListeDesClients(){
        listeClients = new ArrayList<>();
        lireRecupCRUD();
    }

    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<Client> getListeClients() {
        return listeClients;
    }

    public void setListeClients(ArrayList<Client> listeClients) {
        this.listeClients = listeClients;
    }
    
    public void ajouterUnClient(Client C){
        listeClients.add(C);
    }
    
    public void remplacerUnClient(int idAncienClient, Client nouveau){      
        listeClients.set(idAncienClient, nouveau);
    }
    
    public void retirerUnClient(int idClient){
        listeClients.remove(idClient-1);
    }
    
    public boolean estVide(){
        return listeClients.size() <= 0;
    }
    
    public ArrayList<Client> getListeClientsTriee(){
        listeClients.clear();
        lireRecupCRUD();
        return listeClients;
    }
    
    public void afficherListeClientsRecherches(String recherche){
        listeClients.clear();
        chercherCRUD(recherche);
    }

    private void lireRecupCRUD() {
        try {           
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery ("SELECT * FROM clients "
                    + "WHERE est_supprime = 0 ORDER BY nom");
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String prenomJ = rs.getString("prenom");               
                String adresseJ = rs.getString("rue");
                String code_postalJ = rs.getString("code_postal");
                String localiteJ = rs.getString("localite");
                String paysJ = rs.getString("pays");
                String tel_fixeJ = rs.getString("num_telephone");
                String mobileJ = rs.getString("num_mobile");
                String emailJ = rs.getString("email");
                Date date_creationJ = rs.getDate("date_inscription");
                boolean client_fideleJ = rs.getBoolean("client_fidele");
                
                // Création du client à l'aide des informations reçues
                Client recupClient = new Client(idJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, client_fideleJ);
                
                // Ajout du client vers la liste des clients
                ajouterUnClient(recupClient);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Client lireRecupEnreg(Client cli){
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM clients ORDER BY id DESC LIMIT 1");
            if(rs != null)
            {
                rs.next();
                cli.setCode(rs.getInt("id"));
                cli.setNom(rs.getString("nom"));
                cli.setPrenom(rs.getString("prenom"));
                cli.setAdresse(new Adresse(rs.getString("rue"), rs.getString("code_postal"), rs.getString("localite"), rs.getString("pays")));
                cli.setNum_telephone(rs.getString("num_telephone"));
                cli.setNum_mobile(rs.getString("num_mobile"));
                cli.setEmail(rs.getString("email"));
                cli.setDate_inscription(rs.getDate("date_inscription"));
                cli.setClient_fidele(rs.getBoolean("client_fidele"));
                
                // Création du client à l'aide des informations reçues
                Client recupEnregClient = new Client(cli.getCode(), cli.getNom(),
                cli.getPrenom(), cli.getAdresse().getRue(), cli.getAdresse().getCode_postal(), 
                cli.getAdresse().getLocalite(), cli.getAdresse().getPays(),
                cli.getNum_telephone(), cli.getNum_mobile(), cli.getEmail(),
                cli.getDate_inscription(), cli.isClient_fidele());

                // Ajout du client vers la liste des clients
                ajouterUnClient(recupEnregClient); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return cli;
    }
    
    public boolean creerCRUD(Client cli) {
        boolean bCreation = false;
        int                 carte;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "clients (nom, prenom, rue, code_postal, localite, pays, "
                    + "num_telephone, num_mobile, email, date_inscription, client_fidele)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setString(1, cli.getNom());
            prepare.setString(2, cli.getPrenom());
            prepare.setString(3, cli.getAdresse().getRue());
            prepare.setString(4, cli.getAdresse().getCode_postal());
            prepare.setString(5, cli.getAdresse().getLocalite());
            prepare.setString(6, cli.getAdresse().getPays());
            prepare.setString(7, cli.getNum_telephone());
            prepare.setString(8, cli.getNum_mobile());
            prepare.setString(9, cli.getEmail());
            prepare.setString(10, String.valueOf(GestionsDates.dateJavaEnDateSQL(cli.getDate_inscription())));
            
            // Vérification du booléen et assignation à la variable carte
            carte = cli.isClient_fidele() ? 1 : 0;
            prepare.setInt(11, carte);
            
            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
            
            // Récupération de l'enregistrement et ajout dans la liste des clients
            lireRecupEnreg(cli);
            
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
    
    public boolean modifierCRUD(Client cli) {
        boolean bModification = false;
        int carte;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE clients SET"
                    + " nom = ?,"
                    + " prenom = ?,"
                    + " rue = ?,"
                    + " code_postal = ?,"
                    + " localite = ?,"
                    + " pays = ?,"
                    + " num_telephone = ?,"
                    + " num_mobile = ?,"
                    + " email = ?,"
                    + " date_inscription = ?,"
                    + " client_fidele = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, cli.getNom());
            prepare.setString(2, cli.getPrenom());
            prepare.setString(3, cli.getAdresse().getRue());
            prepare.setString(4, cli.getAdresse().getCode_postal());
            prepare.setString(5, cli.getAdresse().getLocalite());
            prepare.setString(6, cli.getAdresse().getPays());
            prepare.setString(7, cli.getNum_telephone());
            prepare.setString(8, cli.getNum_mobile());
            prepare.setString(9, cli.getEmail());
            prepare.setString(10, String.valueOf(GestionsDates.dateJavaEnDateSQL(cli.getDate_inscription())));
            
            // Vérification du booléen et assignation à la variable carte
            carte = cli.isClient_fidele() ? 1 : 0;
            prepare.setInt(11, carte);
            
            prepare.setInt(12, cli.getCode());
            prepare.executeUpdate();
            prepare.close();
            bModification = true;
            
            // Fermeture de la transaction
            laConnexion.setAutoCommit(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Modif dans la BD non effectuée : "
                    + e.getMessage(), "Problème rencontré",
                    JOptionPane.ERROR_MESSAGE);
        }
        return bModification;
    }

    public boolean supprimerCRUD(String vCode){
        boolean bSuppression = false;
        int carte;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE clients SET"
                    + " nom = ?,"
                    + " prenom = ?,"
                    + " rue = ?,"
                    + " code_postal = ?,"
                    + " localite = ?,"
                    + " pays = ?,"
                    + " num_telephone = ?,"
                    + " num_mobile = ?,"
                    + " email = ?,"
                    + " est_supprime = 1"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, "Client supprimé n°" + vCode);
            prepare.setString(2, "");
            prepare.setString(3, "");
            prepare.setString(4, "");
            prepare.setString(5, "");
            prepare.setString(6, "");
            prepare.setString(7, "");
            prepare.setString(8, "");
            prepare.setString(9, "");
            prepare.setInt(10, Integer.parseInt(vCode));
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
    
//    public boolean supprimerCRUD(String vCode) {
//        boolean bSuppression = false;
//        String requete = null;
//        
//        try {
//            // On supprime d'abord la FK dans les Lignes Ticket Caisse
//            requete = "DELETE FROM lignes_ticket_caisse WHERE id_ticket_caisse = ANY"
//                    + "(SELECT id FROM tickets_caisse WHERE id_client = '" + vCode + "')";            
//            Statement state = laConnexion.createStatement();
//            state.executeUpdate(requete);
//            state.close();
//            
//            // Puis on supprime la FK dans Tickets de Caisse
//            requete = "DELETE FROM tickets_caisse WHERE id_client = '" + vCode + "'";
//            Statement state2 = laConnexion.createStatement();
//            state2.executeUpdate(requete);
//            state2.close();
//            
//            // On supprime finalement la ligne de la table Clients
//
//            requete = "DELETE FROM clients WHERE id = '" + vCode + "'";
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
    
    public ArrayList<Client> chercherCRUD(String recherche) {
        String requete = "SELECT * FROM clients"
                + " WHERE nom LIKE '%" + recherche + "%'"
                + " OR prenom LIKE '%" + recherche + "%'";
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery(requete);
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String prenomJ = rs.getString("prenom");               
                String adresseJ = rs.getString("rue");
                String code_postalJ = rs.getString("code_postal");
                String localiteJ = rs.getString("localite");
                String paysJ = rs.getString("pays");
                String tel_fixeJ = rs.getString("num_telephone");
                String mobileJ = rs.getString("num_mobile");
                String emailJ = rs.getString("email");
                Date date_creationJ = rs.getDate("date_inscription");
                boolean client_fideleJ = rs.getBoolean("client_fidele");
                

                // Création du client à l'aide des informations reçues
                Client recupClient = new Client(idJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, client_fideleJ);
                
                // Ajout du client vers la liste des clients
                ajouterUnClient(recupClient);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : "
                    + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return listeClients;
    }
}