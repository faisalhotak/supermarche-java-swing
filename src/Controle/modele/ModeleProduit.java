/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.modele;

import Controle.utili.GestionsDates;
import javax.swing.table.AbstractTableModel;
import Modele.ListeDesProduits;
import Modele.Produit;
import Vue.Fenetre_Gestion;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author faisalhotak
 */
public class ModeleProduit extends AbstractTableModel{
    // pour récupérer les enreg et les mettre dans le modèle
    ListeDesProduits instanceProduit = new ListeDesProduits();
    private ArrayList<Produit> lesDonnees = instanceProduit.getListeProduits();
    // les en-têtes de colonnes
//    private final String[] lesTitres = {"Code", "Nom", "Description", "Code Barres", "Prix", "Taux TVA", "Quantité"};
    private final String[] lesTitres = {"Code", "Nom", "Code Barres", "Prix", "Taux TVA", "Stock"};
    
    @Override
    public int getRowCount(){       
        return lesDonnees.size();
    }
    
    @Override
    public int getColumnCount(){
        return lesTitres.length;
    }
    
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex){
//        switch (columnIndex){
//            case 0 : return lesDonnees.get(rowIndex).getCode();
//            case 1 : return lesDonnees.get(rowIndex).getNom();
//            case 2 : return lesDonnees.get(rowIndex).getDesc_produit();
//            case 3 : return lesDonnees.get(rowIndex).getCode_barre();
//            case 4 : return lesDonnees.get(rowIndex).getPrix();
//            case 5 : return lesDonnees.get(rowIndex).getTaux_tva();
//            case 6 : return lesDonnees.get(rowIndex).getQuantite_produit();
//            default : return null;
//        }
//    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        switch (columnIndex){
            case 0 : return lesDonnees.get(rowIndex).getCode();
            case 1 : return lesDonnees.get(rowIndex).getNom();
            case 2 : return lesDonnees.get(rowIndex).getCode_barre();
            case 3 : return lesDonnees.get(rowIndex).getPrix();
            case 4 : return lesDonnees.get(rowIndex).getTaux_tva();
            case 5 : return lesDonnees.get(rowIndex).getQuantite_produit();
            default : return null;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return lesTitres[columnIndex];
    }
    
    // Pour obtenir le numéro de ligne à partir du code client
    // lors d'une recherche dans l'ArrayList
    public int getNumLigne(String vCode){
        int code;
        int numLigne = 0;
        
        for (int i = 0; i < lesDonnees.size(); i++) {
            code = lesDonnees.get(i).getCode();
            if (String.valueOf(code).equals(vCode)){
                numLigne = i;
            }            
        }
        return numLigne;
    }
    
    public int getRowByCode(int code) {
        int nbLignes = this.getRowCount();
        
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < 1; j++){
                if (this.getValueAt(i, j).equals(code)) {
                    System.out.println("produit trouvé");
                    return i;
                } 
            }
        }        
        return -1;
    }

    public ListeDesProduits getInstanceProduit() {
        return instanceProduit;
    }

    public void setInstanceProduit(ListeDesProduits instanceProduit) {
        this.instanceProduit = instanceProduit;
    }

    public ArrayList<Produit> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<Produit> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }
    
//    public void creerMOD(Client leClient){
//        lesDonnees.add(leClient);
//        // notification d'une création
//        // fireTableRowsInserted(lesDonneees.size() - 1, lesDonnees.size() - 1);
//    }
    
    public boolean creerMOD (Fenetre_Gestion Fen, Produit leProduit){
        boolean insert;
        
        insert = instanceProduit.creerCRUD(leProduit);
        
        if (insert) {
            Fen.setjTxTCode(String.valueOf(leProduit.getCode()));
            
            lesDonnees = instanceProduit.getListeProduitsTriee();          
            fireTableDataChanged();
        }
        return insert;
    }
    
    public boolean supprimerMOD(int rowIndex, String vCode){
        boolean remove;
        remove = instanceProduit.supprimerCRUD(vCode);
        
        if (remove){
            
            lesDonnees.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);    
        }
        return remove;
    }
    
    public boolean modifierMOD(int firstRow, int lastRow, Produit leProduit){
        boolean modified;
        
        modified = instanceProduit.modifierCRUD(leProduit);
        
        if (modified){         
            lesDonnees.set(firstRow, leProduit);
            lesDonnees = instanceProduit.getListeProduitsTriee();
            fireTableRowsUpdated(firstRow, lastRow); 
            
        }
        return modified;
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<Produit> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
        // fireTableDataChanged();
    }
    
    public void rechercherMOD(String recherche){
        instanceProduit.afficherListeProduitsRecherches(recherche);
        fireTableDataChanged();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex){
//            case 3 : return Boolean.class;
//            case 4 : return Date.class;
            default : return Object.class;
        }
    }
}


