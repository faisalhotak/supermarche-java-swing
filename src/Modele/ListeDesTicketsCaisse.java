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
import java.sql.Timestamp;

/**
 *
 * @author faisalhotak
 */

public class ListeDesTicketsCaisse {
    
    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <TicketCaisse> listeTicketsCaisse;

    public ListeDesTicketsCaisse(){
        this.listeTicketsCaisse = new ArrayList<>();
        lireRecupCRUD();
    }
          
    public void ajouterTicketCaisse(TicketCaisse TC){
        listeTicketsCaisse.add(TC);
    }
    
    public void retirerUnTicketCaisse(int idTicketCaisse){
        listeTicketsCaisse.remove(idTicketCaisse);
    }
    
    public boolean estVide(){
        return listeTicketsCaisse.size() <= 0;
    } 
    
        public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<TicketCaisse> getListeTicketsCaisse() {
        return listeTicketsCaisse;
    }

    public void setListeTicketsCaisse (ArrayList<TicketCaisse> listeTicketCaisse) {
        this.listeTicketsCaisse = listeTicketCaisse;
    }
    
    public ArrayList<TicketCaisse> getListeTicketsCaisseTriee(){
        listeTicketsCaisse.clear();
        lireRecupCRUD();       
        return listeTicketsCaisse;
    }
    
    public void afficherListeTicketsRecherches(String recherche){
        listeTicketsCaisse.clear();
        chercherCRUD(recherche);
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();

//              ResultSet rs = state.executeQuery("SELECT * FROM tickets_caisse "
//                      + "ORDER BY date_ticket DESC");
            ResultSet rs = state.executeQuery("SELECT TC.*, C.*, E.*, P.* "
                    + "FROM tickets_caisse TC "
                    + "JOIN clients C ON TC.id_client = C.id "
                    + "JOIN employes E ON TC.id_employe = E.id "
                    + "JOIN postes P ON E.id_poste = P.id "
                    + "ORDER BY TC.date_ticket DESC");
                   
            while (rs.next()) {
                int idJ = rs.getInt("TC.id");
                int idClientJ = rs.getInt("TC.id_client");
                Timestamp dateTicketJ = rs.getTimestamp("TC.date_ticket"); 
                boolean statutJ = rs.getBoolean("TC.statut");               

                int idCliJ = rs.getInt("C.id");
                String nomJ = rs.getString("C.nom");
                String prenomJ = rs.getString("C.prenom");               
                String adresseJ = rs.getString("C.rue");
                String code_postalJ = rs.getString("C.code_postal");
                String localiteJ = rs.getString("C.localite");
                String paysJ = rs.getString("C.pays");
                String tel_fixeJ = rs.getString("C.num_telephone");
                String mobileJ = rs.getString("C.num_mobile");
                String emailJ = rs.getString("C.email");
                Date date_creationJ = rs.getDate("C.date_inscription");
                boolean client_fideleJ = rs.getBoolean("C.client_fidele");

                // Création du client à l'aide des informations reçues
                Client cli = new Client(idCliJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, client_fideleJ);            
                
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
                
                // Création du ticket de caisse à l'aide des informations reçues
                TicketCaisse recupTicketCaisse = new TicketCaisse(idJ, cli, dateTicketJ, statutJ, emp);
                                   
                // Ajout du ticket de caisse vers la liste des tickets de caisse
                ajouterTicketCaisse(recupTicketCaisse);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private TicketCaisse lireRecupEnreg(TicketCaisse tc){
        try {
            Statement state = laConnexion.createStatement();

//            ResultSet rs = state.executeQuery("SELECT * FROM tickets_caisse "
//                    + "ORDER BY id DESC LIMIT 1");
            ResultSet rs = state.executeQuery("SELECT TC.*, C.*, E.*, P.* "
                    + "FROM tickets_caisse TC "
                    + "JOIN clients C ON TC.id_client = C.id "
                    + "JOIN employes E ON TC.id_employe = E.id "
                    + "JOIN postes P ON E.id_poste = P.id "
                    + "ORDER BY TC.id DESC LIMIT 1");
            if(rs != null)
            {
                rs.next();

                tc.setId(rs.getInt("TC.id"));
                tc.setDateDeCreation(rs.getTimestamp("TC.date_ticket"));
                tc.setStatutPaye(rs.getBoolean("TC.statut"));
                
                int idCliJ = rs.getInt("C.id");
                String nomJ = rs.getString("C.nom");
                String prenomJ = rs.getString("C.prenom");               
                String adresseJ = rs.getString("C.rue");
                String code_postalJ = rs.getString("C.code_postal");
                String localiteJ = rs.getString("C.localite");
                String paysJ = rs.getString("C.pays");
                String tel_fixeJ = rs.getString("C.num_telephone");
                String mobileJ = rs.getString("C.num_mobile");
                String emailJ = rs.getString("C.email");
                Date date_creationJ = rs.getDate("C.date_inscription");
                boolean client_fideleJ = rs.getBoolean("C.client_fidele");
                
                // Création du client à l'aide des informations reçues
                Client cli = new Client(idCliJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, client_fideleJ);            
                
                tc.setLeClient(cli);
                
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
                
                tc.setlEmploye(emp);
                
                // Création du ticket à l'aide des informations reçues
                TicketCaisse recupEnregTicketCaisse = new TicketCaisse(tc.getId(),
                tc.getLeClient(), tc.getDateDeCreation(), tc.isStatutPaye(), tc.getlEmploye());

                // Ajout du ticketvers la liste des produits
                ajouterTicketCaisse(recupEnregTicketCaisse); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return tc;
    }
    
    public boolean creerCRUD(TicketCaisse tc) {
        boolean bCreation = false;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "tickets_caisse (id_client, date_ticket, statut, id_employe) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setInt(1, tc.getLeClient().getCode());
            prepare.setTimestamp(2, tc.getDateDeCreation());
            prepare.setBoolean(3, tc.isStatutPaye());
            prepare.setInt(4, tc.getlEmploye().getCode());

            prepare.executeUpdate();
            prepare.close();
            
            
            
            // Validation de la transaction
            laConnexion.commit(); 
            // Récupération de l'enregistrement et ajout dans la liste des clients
            lireRecupEnreg(tc);
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
    
    public boolean modifierCRUD(TicketCaisse tc) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE tickets_caisse SET"
                    + " id_client = ?,"
                    + " date_ticket = ?,"
                    + " statut = ?,"
                    + " id_employe = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setInt(1, tc.getLeClient().getCode());
            prepare.setTimestamp(2, tc.getDateDeCreation());
            prepare.setBoolean(3, tc.isStatutPaye());
            prepare.setInt(4, tc.getlEmploye().getCode());
            prepare.setInt(5, tc.getId());
            
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
//
//    public boolean supprimerCRUD(String vCode) {
//        boolean bModification = false;
//        String requete = null;
//        try {
//            // Ouverture d'une transaction
//            laConnexion.setAutoCommit(false);
//            
//            requete = "UPDATE tickets_caisse SET"
//                    + " est_supprime = 1"
//                    + " WHERE id = ?";
//            
//            PreparedStatement prepare = laConnexion.prepareStatement(requete);
//            
//            prepare.setInt(1, Integer.parseInt(vCode));
//            
//            prepare.executeUpdate();
//            prepare.close();
//            bModification = true;
//            
//            // Fermeture de la transaction
//            laConnexion.setAutoCommit(true);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Modif dans la BD non effectuée : "
//                    + e.getMessage(), "Problème rencontré",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//        return bModification;
//    }
    
    public boolean supprimerCRUD(String vCode) {
        boolean bSuppression = false;
        String requete = null;
        
        try {
            // On supprime ensuite le/les ligne/s
            requete = "DELETE FROM tickets_caisse WHERE id = '" + vCode + "'";
            Statement state2 = laConnexion.createStatement();
            int nbEnregSup = state2.executeUpdate(requete);
            state2.close();

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
    
    public ArrayList<TicketCaisse> chercherCRUD(String recherche) {
        String requete = "SELECT TC.*, C.*, E.*, P.* "
                    + "FROM tickets_caisse TC "
                    + "JOIN clients C ON TC.id_client = C.id "
                    + "JOIN employes E ON TC.id_employe = E.id "
                    + "JOIN postes P ON E.id_poste = P.id "
                    + " WHERE C.nom LIKE '%" + recherche + "%'"
                    + " OR C.prenom LIKE '%" + recherche + "%'"
                    + " OR E.nom LIKE '%" + recherche + "%'"
                    + " OR E.prenom LIKE '%" + recherche + "%'"
                    + " ORDER BY TC.date_ticket DESC";
//   
//        String requete = "SELECT * FROM clients"
//                + " WHERE nom LIKE '%" + recherche + "%'"
//                + " OR prenom LIKE '%" + recherche + "%'";
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery(requete);
            while (rs.next()) {
                int idJ = rs.getInt("TC.id");
                int idClientJ = rs.getInt("TC.id_client");
                Timestamp dateTicketJ = rs.getTimestamp("TC.date_ticket"); 
                boolean statutJ = rs.getBoolean("TC.statut");
                int idEmployeJ = rs.getInt("TC.id_employe");
                int idCliJ = rs.getInt("C.id");
                String nomJ = rs.getString("C.nom");
                String prenomJ = rs.getString("C.prenom");               
                String adresseJ = rs.getString("C.rue");
                String code_postalJ = rs.getString("C.code_postal");
                String localiteJ = rs.getString("C.localite");
                String paysJ = rs.getString("C.pays");
                String tel_fixeJ = rs.getString("C.num_telephone");
                String mobileJ = rs.getString("C.num_mobile");
                String emailJ = rs.getString("C.email");
                Date date_creationJ = rs.getDate("C.date_inscription");
                boolean client_fideleJ = rs.getBoolean("C.client_fidele");

                // Création du client à l'aide des informations reçues
                Client cli = new Client(idCliJ, nomJ, prenomJ, adresseJ, 
                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
                        emailJ, date_creationJ, client_fideleJ);            
                
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
                
                // Création du ticket de caisse à l'aide des informations reçues
                TicketCaisse recupTicketCaisse = new TicketCaisse(idJ, cli, dateTicketJ, statutJ, emp);
                                   
                // Ajout du ticket de caisse vers la liste des tickets de caisse
                ajouterTicketCaisse(recupTicketCaisse);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : "
                    + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return listeTicketsCaisse;
    }
    
}
