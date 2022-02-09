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

/**
 *
 * @author faisalhotak
 */

public class TicketCaisse {

    Connection laConnexion = ControleConnexionSingleton.getConnect();
    ArrayList <LigneTicketCaisse> listeLigneTicketCaisse;
    ListeDesPaiements listePaiements;
    private int id, mode;
    Client leClient;
    Employe lEmploye;
    private Timestamp dateDeCreation;
//    private double montantTotal;
    private boolean statutPaye;
    
    public TicketCaisse(int id, Client client, Timestamp dateDeCreation, boolean statut, Employe employe){
        this.listeLigneTicketCaisse = new ArrayList<>();  
        this.id = id;
        this.dateDeCreation = dateDeCreation;
        this.leClient = client;
        this.statutPaye = statut; 
        this.lEmploye = employe;
        this.listePaiements = new ListeDesPaiements(this);
        lireRecupCRUD();
    }
    
    // Constructeur vide
    public TicketCaisse(){
       this.listeLigneTicketCaisse = new ArrayList<>();
       this.listePaiements = new ListeDesPaiements(this);
    }

    public Timestamp getDateDeCreation() {
        return this.dateDeCreation;
    }

    public void setDateDeCreation(Timestamp dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public BigDecimal getMontantTotal() {
        BigDecimal tot = new BigDecimal(0.0);
        for (LigneTicketCaisse ligneTicketCaisse : listeLigneTicketCaisse) {    
            tot = tot.add(ligneTicketCaisse.getPrixUnitaire().multiply(new BigDecimal(ligneTicketCaisse.getQuantiteAchetee())));
        }
        return tot.setScale(2, RoundingMode.HALF_EVEN);
    }

//    public void setMontantTotal(double montant) {
//        BigDecimal bg = new BigDecimal(montant);
//        bg.setScale(2, RoundingMode.HALF_EVEN);
//        this.montantTotal = bg.doubleValue();
//    }
    
    public void ajouterLigneTicketCaisse(LigneTicketCaisse ltc){
        boolean insert;

        insert = creerCRUD(ltc);
    }
//
//    public void augmenterTotal(double montant){
//        this.montantTotal += (Math.round(montant*100.0)) / 100.0;
//    }
//    
//    public void diminuerTotal(double montant){
//        this.montantTotal -= (Math.round(montant*100.0)) / 100.0;
//    }
    
    
     
    public boolean isStatutPaye() {
        return this.statutPaye;
    }

    public void setStatutPaye(boolean statutPaye) {
        this.statutPaye = statutPaye;
    }
    
    public void paiementTicketCaisse(int id){
        this.setStatutPaye(true);
        this.mode = id;
    }
    
    public boolean estVide(){
        return listeLigneTicketCaisse.size() <= 0;
    }
    
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getLeClient() {
        return leClient;
    }

    public void setLeClient(Client leClient) {
        this.leClient = leClient;
    }

    public Employe getlEmploye() {
        return lEmploye;
    }

    public void setlEmploye(Employe lEmploye) {
        this.lEmploye = lEmploye;
    }

    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<LigneTicketCaisse> getListeLigneTicketCaisse() {
        return listeLigneTicketCaisse;
    }

    public void setListeLigneTicketCaisse(ArrayList<LigneTicketCaisse> listeLigneTicketCaisse) {
        this.listeLigneTicketCaisse = listeLigneTicketCaisse;
    }
    
    public ArrayList<LigneTicketCaisse> getListeLigneTicketsCaisseTriee(){
        listeLigneTicketCaisse.clear();
        lireRecupCRUD();
        return listeLigneTicketCaisse;
    }

    public ListeDesPaiements getListePaiements() {
        return listePaiements;
    }

    public void setListePaiements(ListeDesPaiements listePaiements) {
        this.listePaiements = listePaiements;
    }

    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();

            ResultSet rs = state.executeQuery("SELECT LTC.*, P.nom, "
                        + "P.desc_produit, P.code_barres, P.prix, "
                        + "P.quantite_produit, P.taux_tva "
                        + "FROM lignes_ticket_caisse LTC "
                        + "JOIN produits P ON LTC.id_produit = P.id "
                        + "WHERE LTC.id_ticket_caisse = '" + this.id + "'"
                        + " ORDER BY LTC.id");
                   
            while (rs.next()) {
                int idJ = rs.getInt("id");
                int idTicketCaisseJ = rs.getInt("id_ticket_caisse");
                int idProduitJ = rs.getInt("id_produit");
                int quantiteAcheteeJ = rs.getInt("quantite_achetee");
                BigDecimal prixUnitaireJ = rs.getBigDecimal("prix_unitaire");
                
                String nomJ = rs.getString("nom");
                String descJ = rs.getString("desc_produit");
                String codeBarresJ = rs.getString("code_barres");
                double prixJ = rs.getDouble("prix");
                double tauxTVAJ = rs.getDouble("taux_tva");
                int quantitePJ = rs.getInt("quantite_produit");
                
                // Création du produit associé
                Produit leProduit = new Produit(idProduitJ, nomJ, descJ, codeBarresJ, prixJ, tauxTVAJ, quantitePJ);
                
                // Création de la ligne du ticket de caisse à l'aide des informations reçues
                LigneTicketCaisse recupLigneTicketCaisse = new LigneTicketCaisse(idJ, idTicketCaisseJ, leProduit, prixUnitaireJ, quantiteAcheteeJ);
                                   
                // Ajout de la ligne du ticket de caisse vers la liste des lignes de ticket de caisse
                this.listeLigneTicketCaisse.add(recupLigneTicketCaisse);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private LigneTicketCaisse lireRecupEnreg(LigneTicketCaisse ltc){
        try {
            Statement state = laConnexion.createStatement();

            ResultSet rs = state.executeQuery("SELECT LTC.*, P.nom, "
                      + "P.desc_produit, P.code_barres, P.prix, "
                      + "P.quantite_produit, P.taux_tva "
                      + "FROM lignes_ticket_caisse LTC "
                      + "JOIN produits P ON LTC.id_produit = P.id "
                      + "ORDER BY LTC.id DESC LIMIT 1");
            
            if(rs != null)
            {
                rs.next();

                ltc.setId(rs.getInt("id"));
                ltc.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                ltc.setQuantiteAchetee(rs.getInt("quantite_achetee"));
                
                String nomJ = rs.getString("nom");
                String descJ = rs.getString("desc_produit");
                String codeBarresJ = rs.getString("code_barres");
                double prixJ = rs.getDouble("prix");
                double tauxTVAJ = rs.getDouble("taux_tva");
                int quantitePJ = rs.getInt("quantite_produit");
                
                // Création du produit associé
                Produit leProduit = new Produit(ltc.getId(), nomJ, descJ, codeBarresJ, prixJ, tauxTVAJ, quantitePJ);
                ltc.setLeProduit(leProduit);
                
                // Création de la ligne du ticket de caisse à l'aide des informations reçues
                LigneTicketCaisse recupLigneTicketCaisse = new LigneTicketCaisse(ltc.getId(), ltc.getId_ticket_caisse(), ltc.getLeProduit(), ltc.getPrixUnitaire(), ltc.getQuantiteAchetee());
                
                // Ajout de la ligne du ticket de caisse vers la liste des lignes de ticket de caisse
                this.listeLigneTicketCaisse.add(recupLigneTicketCaisse); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return ltc;
    }
    
    public boolean creerCRUD(LigneTicketCaisse ltc) {
        boolean bCreation = false;
        String requete = null;

        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "lignes_ticket_caisse (id_ticket_caisse, id_produit, quantite_achetee, prix_unitaire) "
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            prepare.setInt(1, ltc.getId_ticket_caisse());
            prepare.setInt(2, ltc.getLeProduit().getCode());
            prepare.setInt(3, ltc.getQuantiteAchetee());
            prepare.setBigDecimal(4, ltc.getPrixUnitaire().setScale(2, RoundingMode.HALF_EVEN));

            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
            
            // Récupération de l'enregistrement et ajout dans la liste des lignes de ticket de caisse
            lireRecupEnreg(ltc);
            
            bCreation = true;
            
            // Fermeture de la transaction
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
    
    public boolean modifierCRUD(LigneTicketCaisse ltc) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE lignes_ticket_caisse SET"
                    + " id_ticket_caisse = ?,"
                    + " id_produit = ?,"
                    + " quantite_achetee = ?,"
                    + " prix_unitaire = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setInt(1, ltc.getId_ticket_caisse());
            prepare.setInt(2, ltc.getLeProduit().getCode());
            prepare.setInt(3, ltc.getQuantiteAchetee());
            prepare.setBigDecimal(4, ltc.getPrixUnitaire().setScale(2, RoundingMode.HALF_EVEN));
            
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
            requete = "DELETE FROM lignes_ticket_caisse WHERE id = '" + vCode + "'";
            Statement state = laConnexion.createStatement();
            int nbEnregSup = state.executeUpdate(requete);

            state.close();

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
