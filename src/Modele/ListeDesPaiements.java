package Modele;

import Controle.ControleConnexionSingleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.Timestamp;

public class ListeDesPaiements {
        
    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <Paiement> listePaiements;
    
    public ListeDesPaiements(TicketCaisse tc){
        listePaiements = new ArrayList<>();
        lireRecupCRUD(tc);
    }
    
    public void ajouterUnPaiement(Paiement P){
        listePaiements.add(P);
    }
    
    public void remplacerUnPaiement(int idAncienPaiement, Paiement nouveau){      
        listePaiements.set(idAncienPaiement, nouveau);
    }
    
    public void retirerUnPaiement(int idPaiement){
        listePaiements.remove(idPaiement-1);
    }
    
    public boolean estVide(){
        return listePaiements.size() <= 0;
    }
    
    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<Paiement> getListePaiements() {
        return listePaiements;
    }

    public void setListePaiements(ArrayList<Paiement> listePaiements) {
        this.listePaiements = listePaiements;
    }
    
//    public ArrayList<Paiement> getListePaiementsTriee(){
//        listePaiements.clear();
//        lireRecupCRUD();
//        return listePaiements;
//    }
    
    public BigDecimal getMontantTotalPaiements(){
        BigDecimal tot = new BigDecimal(0.0);
        for (Paiement p : listePaiements) {
            tot = tot.add(p.getMontant());
        }
        return tot.setScale(2, RoundingMode.HALF_EVEN);
    }
    
    private void lireRecupCRUD(TicketCaisse ticketCaisse) {
        try {
            Statement state = laConnexion.createStatement();

              ResultSet rs = state.executeQuery("SELECT P.*, MP.* FROM paiements P "
                      + "JOIN modes_paiement MP ON MP.id = P.id_mode_paiement "
                      + "WHERE P.id_ticket_caisse = '" + ticketCaisse.getId() + "'"
                      + " ORDER BY P.date_paiement");
                   
            while (rs.next()) {
                int idJ = rs.getInt("P.id");
                int idTicketCaisseJ = rs.getInt("P.id_ticket_caisse");
                int idModePaiementJ = rs.getInt("MP.id");
                String modePaiementJ = rs.getString("MP.mode");
                Timestamp datePaiementJ = rs.getTimestamp("P.date_paiement");
                BigDecimal montantJ = rs.getBigDecimal("P.montant");
                
                // Création du mode
                ModePaiement leModePaiement = new ModePaiement(idModePaiementJ, modePaiementJ);
                
                // Création ddu produit à l'aide des informations reçues
                Paiement recupPaiement = new Paiement(idJ, ticketCaisse, leModePaiement, datePaiementJ, montantJ);
                                   
                // Ajout du produit à la liste des produits
                ajouterUnPaiement(recupPaiement);
               
            }
            
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Paiement lireRecupEnreg(Paiement paiement){
        try {
            Statement state = laConnexion.createStatement();

            ResultSet rs = state.executeQuery("SELECT P.*, MP.* FROM paiements P "
                      + "JOIN modes_paiement MP ON MP.id = P.id_mode_paiement"
                      + " ORDER BY P.id DESC LIMIT 1");

            if(rs != null)
            {
                rs.next();
                
                paiement.setId(rs.getInt("P.id"));
                paiement.getTicket_caisse().setId(rs.getInt("P.id_ticket_caisse")); 
                int idModePaiementJ = rs.getInt("MP.id");
                String modePaiementJ = rs.getString("MP.mode");

                paiement.setDate_paiement(rs.getTimestamp("date_paiement"));
                paiement.setMontant(rs.getBigDecimal("montant"));
                
                // Création du mode
                ModePaiement leModePaiement = new ModePaiement(idModePaiementJ, modePaiementJ);
                paiement.setMode_paiement(leModePaiement);

                // Création du paiement
                Paiement recupEnregPaiement = new Paiement(paiement.getId(), paiement.getTicket_caisse(), 
                        paiement.getMode_paiement(), paiement.getDate_paiement(), 
                        paiement.getMontant());
                
                // Ajout du produit vers la liste des produits
                ajouterUnPaiement(recupEnregPaiement); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return paiement;
    }
    
    public boolean creerCRUD(Paiement paiement) {
        boolean bCreation = false;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "paiements (id_ticket_caisse, id_mode_paiement, date_paiement, montant)"
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setInt(1, paiement.getTicket_caisse().getId());
            prepare.setInt(2, paiement.getMode_paiement().getId());
            prepare.setTimestamp(3, paiement.getDate_paiement());
            prepare.setBigDecimal(4, paiement.getMontant());

            
            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
   
//            // Récupération de l'enregistrement et ajout dans la liste des clients
            lireRecupEnreg(paiement);


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
    
    public boolean modifierCRUD(Paiement paiement) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE produits SET"
                    + " id_ticket_caisse = ?,"
                    + " id_mode_paiement = ?,"
                    + " date_paiement = ?,"
                    + " montant = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setInt(1, paiement.getTicket_caisse().getId());
            prepare.setInt(2, paiement.getMode_paiement().getId());
            prepare.setTimestamp(3, paiement.getDate_paiement());
            prepare.setBigDecimal(4, paiement.getMontant());
            prepare.setInt(5, paiement.getId());
            
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

    public boolean supprimerCRUD(String vCode) {
        boolean bSuppression = false;
        String requete = null;
        
        try {
            requete = "DELETE FROM paiements WHERE id = '" + vCode + "'";
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
    
//    public ArrayList<Client> chercherCRUD(String recherche) {
//        String requete = "SELECT * FROM clients"
//                + " WHERE nom LIKE '%" + recherche + "%'"
//                + " OR prenom LIKE '%" + recherche + "%'";
//        try {
//            Statement state = laConnexion.createStatement();
//            ResultSet rs = state.executeQuery(requete);
//            while (rs.next()) {
//                String idJ = rs.getString("id");
//                String nomJ = rs.getString("nom");
//                String prenomJ = rs.getString("prenom");               
//                String adresseJ = rs.getString("rue");
//                String code_postalJ = rs.getString("code_postal");
//                String localiteJ = rs.getString("localite");
//                String paysJ = rs.getString("pays");
//                String tel_fixeJ = rs.getString("num_fixe");
//                String mobileJ = rs.getString("num_mobile");
//                String emailJ = rs.getString("email");
//                Date date_creationJ = rs.getDate("date_inscription");
//                boolean client_fideleJ = rs.getBoolean("carte_fidele");
//                
//                // Création du client à l'aide des informations reçues
//                Client recupClient = new Client(idJ, nomJ, prenomJ, adresseJ, 
//                        code_postalJ, localiteJ, paysJ, tel_fixeJ, mobileJ, 
//                        emailJ, date_creationJ, client_fideleJ);
//                
//                // Ajout du client vers la liste des clients
//                ajouterUnClient(recupClient);
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null,
//                    "Problème rencontré : "
//                    + e.getMessage(),
//                    "Résultat", JOptionPane.ERROR_MESSAGE);
//        }
//        return listeClients;
//    }
}