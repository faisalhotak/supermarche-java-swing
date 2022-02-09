package Controle.modele;

import Modele.LigneTicketCaisse;
import javax.swing.table.AbstractTableModel;
import Modele.TicketCaisse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ModeleLigneTicketCaisse extends AbstractTableModel{
    // pour récupérer les enreg et les mettre dans le modèle
    TicketCaisse instanceTicket;
    private ArrayList<LigneTicketCaisse> lesDonnees;
    // les en-têtes de colonnes
    private final String[] lesTitres = {"Quantité Achetée", "Nom du Produit", "Prix Unitaire", "Total"};
    
    public ModeleLigneTicketCaisse(TicketCaisse ticket){
        this.instanceTicket = ticket;
        this.lesDonnees = this.instanceTicket.getListeLigneTicketCaisse();
    }
    
    // Constructeur vide
    public ModeleLigneTicketCaisse(){
        this.instanceTicket = new TicketCaisse();
        this.lesDonnees = this.instanceTicket.getListeLigneTicketCaisse();
    }
    
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
            case 0 : return lesDonnees.get(rowIndex).getQuantiteAchetee();
            case 1 : return lesDonnees.get(rowIndex).getLeProduit().getNom();
            case 2 : return lesDonnees.get(rowIndex).getPrixUnitaire();
            case 3 : BigDecimal resultat = (lesDonnees.get(rowIndex).getPrixUnitaire().multiply(new BigDecimal(lesDonnees.get(rowIndex).getQuantiteAchetee())));
                     resultat = resultat.setScale(2, RoundingMode.HALF_EVEN);
                     return resultat;
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
        String code = "";
        int numLigne = 0;
        
        for (int i = 0; i < lesDonnees.size(); i++) {
            code = String.valueOf(lesDonnees.get(i).getId());
            if (code.equals(vCode)){
                numLigne = i;
            }            
        }
        return numLigne;
    }
    
    public int getRowByCode(String code) {
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
    
    public void clearModele(){
        this.lesDonnees.clear();
        fireTableDataChanged();
    }

    public TicketCaisse getInstanceTicket() {
        return instanceTicket;
    }

    public void setInstanceTicket(TicketCaisse instanceTicket) {
        this.instanceTicket = instanceTicket;
    }

    public ArrayList<LigneTicketCaisse> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<LigneTicketCaisse> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }
    
    public void ajouterLigneTicketCaisse(LigneTicketCaisse ltc){
        this.lesDonnees.add(ltc);
        fireTableDataChanged();
    }
    
    public void retirerLigneTicketCaisse(int index, int quantite){
        this.lesDonnees.get(index).diminuerQuantite(quantite);
        if (lesDonnees.get(index).getQuantiteAchetee() < 1){
            this.lesDonnees.remove(index);
        }
        fireTableDataChanged();
    }
    
    public boolean creerMOD (LigneTicketCaisse laLigne){
        boolean insert;
        
        insert = instanceTicket.creerCRUD(laLigne);
        
        if (insert) {            
            lesDonnees = instanceTicket.getListeLigneTicketsCaisseTriee();
//            fireTableDataChanged();
        }
        return insert;
    }
    
    public boolean supprimerMOD(int rowIndex, String vCode){
        boolean remove;
        remove = instanceTicket.supprimerCRUD(vCode);
        
        if (remove){
            
            lesDonnees.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);    
        }
        return remove;
    }
    
    public boolean modifierMOD(int firstRow, int lastRow, LigneTicketCaisse laLigne){
        boolean modified;
        
        modified = instanceTicket.modifierCRUD(laLigne);
        
        if (modified){         
            lesDonnees.set(firstRow, laLigne);
            fireTableRowsUpdated(firstRow, lastRow); 
            
        }
        return modified;
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<LigneTicketCaisse> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
        // fireTableDataChanged();
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex){
//            case 3 : return Boolean.class;
//            case 2 : return Date.class;
            default : return Object.class;
        }
    }
}


