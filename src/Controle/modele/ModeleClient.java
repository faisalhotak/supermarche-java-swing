package Controle.modele;

import javax.swing.table.AbstractTableModel;
import Modele.Client;
import Modele.ListeDesClients;
import Vue.Fenetre_Gestion;
import java.util.ArrayList;
import java.util.Date;

public class ModeleClient extends AbstractTableModel{
    // pour récupérer les enreg et les mettre dans le modèle
    ListeDesClients instanceClient = new ListeDesClients();
    private ArrayList<Client> lesDonnees = instanceClient.getListeClients();
    // les en-têtes de colonnes
    private final String[] lesTitres = {"Code", "Nom", "Prénom", "Client Fidèle", "Date Création"};
    
    @Override
    public int getRowCount(){       
        return lesDonnees.size();
    }
    
    @Override
    public int getColumnCount(){
        return lesTitres.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        switch (columnIndex){
            case 0 : return lesDonnees.get(rowIndex).getCode();
            case 1 : return lesDonnees.get(rowIndex).getNom();
            case 2 : return lesDonnees.get(rowIndex).getPrenom();
            case 3 : return lesDonnees.get(rowIndex).isClient_fidele();
            case 4 : return lesDonnees.get(rowIndex).getDate_inscription();
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

    public ListeDesClients getInstanceClient() {
        return instanceClient;
    }

    public void setInstanceClient(ListeDesClients instanceClient) {
        this.instanceClient = instanceClient;
    }

    public ArrayList<Client> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<Client> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }

    public boolean creerMOD (Fenetre_Gestion Fen, Client leClient){
        boolean insert;
        
        insert = instanceClient.creerCRUD(leClient);
        
        if (insert) {
            Fen.setjTxTCode(String.valueOf(leClient.getCode()));
            
            lesDonnees = instanceClient.getListeClientsTriee();
            fireTableDataChanged();
        }
        return insert;
    }
    
    public boolean supprimerMOD(int indexRow, String vCode){
        boolean remove;
        remove = instanceClient.supprimerCRUD(vCode);
        
        if (remove){
            lesDonnees.remove(indexRow);
            this.fireTableRowsDeleted(indexRow, indexRow);
        }
        return remove;
    }
    
    public boolean modifierMOD(int firstRow, int lastRow, Client leClient){
        boolean modified;
        
        modified = instanceClient.modifierCRUD(leClient);
        
        if (modified){         
            lesDonnees.set(firstRow, leClient);
            lesDonnees = instanceClient.getListeClientsTriee();
            fireTableRowsUpdated(firstRow, lastRow); 
        }
        return modified;
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<Client> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
        // fireTableDataChanged();
    }
    
    public void rechercherMOD(String recherche){
        instanceClient.afficherListeClientsRecherches(recherche);
        fireTableDataChanged();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex){
            case 3 : return Boolean.class;
            case 4 : return Date.class;
            default : return Object.class;
        }
    }
}


