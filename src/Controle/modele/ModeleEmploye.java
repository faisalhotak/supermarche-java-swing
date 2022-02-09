/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle.modele;

import javax.swing.table.AbstractTableModel;
import Modele.Employe;
import Modele.ListeDesEmployes;
import Vue.Fenetre_Gestion;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author faisalhotak
 */
public class ModeleEmploye extends AbstractTableModel{
    // pour récupérer les enreg et les mettre dans le modèle
    ListeDesEmployes instanceEmploye = new ListeDesEmployes();
    private ArrayList<Employe> lesDonnees = instanceEmploye.getListeEmployes();
    // les en-têtes de colonnes
//    private final String[] lesTitres = {"Code", "Nom", "Prénom", "Poste", "Date d'embauche", "Adresse", "Code Postal", "Localité", "Pays", "Téléphone", "Mobile", "Salaire", "Email"};
    private final String[] lesTitres = {"Code", "Nom", "Prénom", "Poste", "Date d'embauche"};
    
    @Override
    public int getRowCount(){       
        return lesDonnees.size();
    }
    
    @Override
    public int getColumnCount(){
        return lesTitres.length;
    }
    
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex)
//    {
//        switch (columnIndex){
//            case 0 : return lesDonnees.get(rowIndex).getCode();
//            case 1 : return lesDonnees.get(rowIndex).getNom();
//            case 2 : return lesDonnees.get(rowIndex).getPrenom();
//            case 3 : return lesDonnees.get(rowIndex).getPoste();
//            case 4 : return lesDonnees.get(rowIndex).getDate_embauche();
//            case 5 : return lesDonnees.get(rowIndex).getAdresse().getRue();
//            case 6 : return lesDonnees.get(rowIndex).getAdresse().getCode_postal();
//            case 7 : return lesDonnees.get(rowIndex).getAdresse().getLocalite();
//            case 8 : return lesDonnees.get(rowIndex).getAdresse().getPays();
//            case 9 : return lesDonnees.get(rowIndex).getNum_telephone();
//            case 10 : return lesDonnees.get(rowIndex).getNum_mobile();
//            case 11 : return lesDonnees.get(rowIndex).getSalaire();
//            case 12 : return lesDonnees.get(rowIndex).getEmail();
//            
//            default : return null;
//        }
//    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex){
            case 0 : return lesDonnees.get(rowIndex).getCode();
            case 1 : return lesDonnees.get(rowIndex).getNom();
            case 2 : return lesDonnees.get(rowIndex).getPrenom();
            case 3 : return lesDonnees.get(rowIndex).getPoste();
            case 4 : return lesDonnees.get(rowIndex).getDate_embauche();
            
            default : return null;
        }
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return lesTitres[columnIndex];
    }
    
    public int getRowByCode(int code) {
        int nbLignes = this.getRowCount();
        
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < 1; j++){
                if (this.getValueAt(i, j).equals(code)) {
                    return i;
                } 
            }
        }        
        return -1;
    }

    public ListeDesEmployes getInstanceEmploye() {
        return instanceEmploye;
    }

    public void setInstanceEmploye(ListeDesEmployes instanceEmploye) {
        this.instanceEmploye = instanceEmploye;
    }

    public ArrayList<Employe> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<Employe> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }
    
//    public void creerMOD(Client leClient){
//        lesDonnees.add(leClient);
//        // notification d'une création
//        // fireTableRowsInserted(lesDonneees.size() - 1, lesDonnees.size() - 1);
//    }
    
    public boolean creerMOD (Fenetre_Gestion Fen, Employe lEmploye){
        boolean insert;
        
        insert = instanceEmploye.creerCRUD(lEmploye);
        
        if (insert) {
            Fen.setjTxTCode(String.valueOf(lEmploye.getCode()));
            
            lesDonnees = instanceEmploye.getListeEmployesTriee();
            fireTableDataChanged();
        }
        return insert;
    }
    
    public boolean supprimerMOD(int rowIndex, String vCode){
        boolean remove;
        remove = instanceEmploye.supprimerCRUD(vCode);
        
        if (remove){
            
            lesDonnees.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);    
        }
        return remove;
    }
    
    public boolean modifierMOD(int firstRow, int lastRow, Employe lEmploye){
        boolean modified;
        
        modified = instanceEmploye.modifierCRUD(lEmploye);
        
        if (modified){         
            lesDonnees.set(firstRow, lEmploye);
            lesDonnees = instanceEmploye.getListeEmployesTriee();
            fireTableRowsUpdated(firstRow, lastRow); 
        }
        return modified;
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<Employe> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
        // fireTableDataChanged();
    }
    
    public void rechercherMOD(String recherche){
        instanceEmploye.afficherListeEmployesRecherches(recherche);
        fireTableDataChanged();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex){
//            case 3 : return Boolean.class;
            case 4 : return Date.class;
            default : return Object.class;
        }
    }
}


