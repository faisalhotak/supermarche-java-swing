package Modele;

import Controle.ControleConnexionSingleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author faisalhotak
 */

public class ListeDesProduits {
        
    private Connection laConnexion = ControleConnexionSingleton.getConnect();
    private ArrayList <Produit> listeProduits;
    
    public ListeDesProduits(){
        listeProduits = new ArrayList<>();
        lireRecupCRUD();
    }
    
    public ListeDesProduits(int v){
        listeProduits = new ArrayList<>();
    }
    
    public void ajouterUnProduit(Produit P){
        listeProduits.add(P);
    }
    
    public void remplacerUnProduit(int idAncienProduit, Produit nouveau){      
        listeProduits.set(idAncienProduit, nouveau);
    }
    
    public void retirerUnProduit(int idProduit){
        listeProduits.remove(idProduit-1);
    }
    
    public boolean estVide(){
        return listeProduits.size() <= 0;
    }
    
    public void remettreProduitEnStock(int idProduit, int quantite){
        listeProduits.get(idProduit-1).augmenterQuantite_produit(quantite);
    }    

    public Connection getLaConnexion() {
        return laConnexion;
    }

    public void setLaConnexion(Connection laConnexion) {
        this.laConnexion = laConnexion;
    }

    public ArrayList<Produit> getListeProduits() {
        return listeProduits;
    }

    public void setListeProduits(ArrayList<Produit> listeProduits) {
        this.listeProduits = listeProduits;
    }
    
    public ArrayList<Produit> getListeProduitsTriee(){
        listeProduits.clear();
        lireRecupCRUD();
        return listeProduits;
    }
    
    public void afficherListeProduitsRecherches(String recherche){
        listeProduits.clear();
        chercherCRUD(recherche);
    }
    
    private void lireRecupCRUD() {
        try {
            Statement state = laConnexion.createStatement();

            ResultSet rs = state.executeQuery("SELECT * FROM produits "
                      + "WHERE est_supprime = 0 ORDER BY nom");
                   
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String desc = rs.getString("desc_produit"); 
                String code_barres = rs.getString("code_barres");
                double prix = rs.getDouble("prix");
                double taux = rs.getDouble("taux_tva");
                int quantite = rs.getInt("quantite_produit");              
                
                // Création ddu produit à l'aide des informations reçues
                Produit recupProduit = new Produit(idJ, nomJ, desc, code_barres, 
                        prix, taux, quantite);
                                   
                // Ajout du produit à la liste des produits
                ajouterUnProduit(recupProduit);
            }
            state.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : " + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Produit lireRecupEnreg(Produit pro){
        try {
            Statement state = laConnexion.createStatement();

            ResultSet rs = state.executeQuery("SELECT * FROM produits "
                    + "ORDER BY id DESC LIMIT 1");
            if(rs != null)
            {
                rs.next();
                pro.setCode(rs.getInt("id"));
                pro.setNom(rs.getString("nom"));
                pro.setDesc_produit(rs.getString("desc_produit"));
                pro.setCode_barre(rs.getString("code_barres"));
                pro.setPrix(rs.getDouble("prix"));
                pro.setTaux_tva(rs.getDouble("taux_tva"));
                pro.setQuantite_produit(rs.getInt("quantite_produit"));
                
                // Création du produit à l'aide des informations reçues
                Produit recupEnregProduit = new Produit(pro.getCode(),
                pro.getNom(), pro.getDesc_produit(), pro.getCode_barre(),
                pro.getPrix(), pro.getTaux_tva(), pro.getQuantite_produit());

                // Ajout du produit vers la liste des produits
                ajouterUnProduit(recupEnregProduit); 
            }
            state.close();
        } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Problème rencontré : " + e.getMessage(),
                "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return pro;
    }
    
    public boolean creerCRUD(Produit pro) {
        boolean bCreation = false;
        int                 carte;
        String requete = null;
        
        //valueLock.lock();
        try {
            laConnexion.setAutoCommit(false);//ouverture d'une transaction
            
            requete = "INSERT INTO "
                    + "produits (nom, desc_produit, code_barres, prix, taux_tva, "
                    + "quantite_produit)"
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
                        
            prepare.setString(1, pro.getNom());
            prepare.setString(2, pro.getDesc_produit());
            prepare.setString(3, pro.getCode_barre());
            prepare.setDouble(4, pro.getPrix());
            prepare.setDouble(5, pro.getTaux_tva());
            prepare.setInt(6, pro.getQuantite_produit());

            
            prepare.executeUpdate();
            prepare.close();
            
            // Validation de la transaction
            laConnexion.commit(); 
            
            // Récupération de l'enregistrement et ajout dans la liste des clients
            lireRecupEnreg(pro);
            
            bCreation = true;
            
            // Fermetrue de la transaction
            laConnexion.setAutoCommit(true);          
        } 
        catch (SQLException e) 
        {
            //laConnexion.rollback();// c'est ici que l'on annule la transaction 
            //laConnexion.setAutoCommit(true); //fermeture de la transaction
            
            if (e.getErrorCode() == 1062){
                JOptionPane.showMessageDialog(null,
                    "Ce nom de produit existe déjà, veuillez lui attribuer un autre nom !"
                    , "Problème rencontré",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null,
                    "Ajout dans la BD non effectué : "
                    + e.getMessage(), "Problème rencontré",
                    JOptionPane.ERROR_MESSAGE);
            }            
        }   
        /* finally {
            valueLock.unlock();
        */
        return bCreation;
    }
    
    public boolean modifierCRUD(Produit pro) {
        boolean bModification = false;
        String requete = null;
        try {
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE produits SET"
                    + " nom = ?,"
                    + " desc_produit = ?,"
                    + " code_barres = ?,"
                    + " prix = ?,"
                    + " taux_tva = ?,"
                    + " quantite_produit = ?"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, pro.getNom());
            prepare.setString(2, pro.getDesc_produit());
            prepare.setString(3, pro.getCode_barre());
            prepare.setDouble(4, pro.getPrix());
            prepare.setDouble(5, pro.getTaux_tva());
            prepare.setInt(6, pro.getQuantite_produit());                  
            prepare.setInt(7, pro.getCode());
            
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
            // Ouverture d'une transaction
            laConnexion.setAutoCommit(false);
            
            requete = "UPDATE produits SET"
                    + " est_supprime = true"
                    + " WHERE id = ?";
            
            PreparedStatement prepare = laConnexion.prepareStatement(requete);
            
            prepare.setString(1, vCode);                 
            
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
//            requete = "DELETE FROM lignes_ticket_caisse WHERE id_produit = '" + vCode + "'";            
//            Statement state = laConnexion.createStatement();
//            state.executeUpdate(requete);
//            state.close();
//            
//            // On supprime ensuite le/les produit/s
//            requete = "DELETE FROM produits WHERE id = '" + vCode + "'";
//            Statement state2 = laConnexion.createStatement();
//            int nbEnregSup = state2.executeUpdate(requete);
//            state2.close();
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
    
    public ArrayList<Produit> chercherCRUD(String recherche) {
        String requete = "SELECT * FROM produits "                     
                        + " WHERE nom LIKE '%" + recherche + "%'"
                        + " OR desc_produit LIKE '%" + recherche + "%'"
                        + " OR code_barres LIKE '%" + recherche + "%'"
                        + " ORDER BY nom";
        try {
            Statement state = laConnexion.createStatement();
            ResultSet rs = state.executeQuery(requete);
            while (rs.next()) {
                int idJ = rs.getInt("id");
                String nomJ = rs.getString("nom");
                String desc = rs.getString("desc_produit"); 
                String code_barres = rs.getString("code_barres");
                double prix = rs.getDouble("prix");
                double taux = rs.getDouble("taux_tva");
                int quantite = rs.getInt("quantite_produit");              
                
                // Création ddu produit à l'aide des informations reçues
                Produit recupProduit = new Produit(idJ, nomJ, desc, code_barres, 
                        prix, taux, quantite);
                                   
                // Ajout du produit à la liste des produits
                ajouterUnProduit(recupProduit);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré : "
                    + e.getMessage(),
                    "Résultat", JOptionPane.ERROR_MESSAGE);
        }
        return listeProduits;
    }
}