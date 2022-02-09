package Controle.modele;

import javax.swing.table.AbstractTableModel;
import Modele.ListeDesTicketsCaisse;
import Modele.TicketCaisse;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ModeleTicketCaisse extends AbstractTableModel{
    
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)");
    // pour récupérer les enreg et les mettre dans le modèle
    ListeDesTicketsCaisse instanceTicket = new ListeDesTicketsCaisse();
    private ArrayList<TicketCaisse> lesDonnees = instanceTicket.getListeTicketsCaisse();
    // les en-têtes de colonnes
    private final String[] lesTitres = {"Numéro du Ticket", "Date du Ticket", "Client Associé", "Employé Associé", "Payé En Totalité ?", "Montant Total", "Montant Payé", "Montant Restant"};
    
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
            case 0 : return lesDonnees.get(rowIndex).getId();
            case 1 : return format.format(lesDonnees.get(rowIndex).getDateDeCreation());
            case 2 : return lesDonnees.get(rowIndex).getLeClient().getNom() + " " + lesDonnees.get(rowIndex).getLeClient().getPrenom();
            case 3 : return lesDonnees.get(rowIndex).getlEmploye().getNom() + " " + lesDonnees.get(rowIndex).getlEmploye().getPrenom();
            case 4 : return lesDonnees.get(rowIndex).isStatutPaye();
            case 5 : return lesDonnees.get(rowIndex).getMontantTotal().setScale(2, RoundingMode.HALF_EVEN);          
            case 6 : return lesDonnees.get(rowIndex).getListePaiements().getMontantTotalPaiements().setScale(2, RoundingMode.HALF_EVEN);
            case 7 : return lesDonnees.get(rowIndex).getMontantTotal().subtract(lesDonnees.get(rowIndex).getListePaiements().getMontantTotalPaiements()).setScale(2, RoundingMode.HALF_EVEN);            
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

    public ListeDesTicketsCaisse getInstanceTicket() {
        return instanceTicket;
    }

    public void setInstanceTicket(ListeDesTicketsCaisse instanceTicket) {
        this.instanceTicket = instanceTicket;
    }

    public ArrayList<TicketCaisse> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<TicketCaisse> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }
    
    public boolean creerMOD (TicketCaisse leTicket){
        boolean insert;
        
        insert = instanceTicket.creerCRUD(leTicket);
        
        if (insert) {           
           
            lesDonnees = instanceTicket.getListeTicketsCaisseTriee();
            fireTableDataChanged();
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
    
    public boolean modifierMOD(int firstRow, int lastRow, TicketCaisse leTicket){
        boolean modified;
        
        modified = instanceTicket.modifierCRUD(leTicket);
        
        if (modified){         
            lesDonnees.set(firstRow, leTicket);
            fireTableRowsUpdated(firstRow, lastRow);     
        }
        return modified;
    }
    
    public void rafraichirMOD(){       
        fireTableDataChanged();
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<TicketCaisse> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
        // fireTableDataChanged();
    }
    
    public void rechercherMOD(String recherche){
        instanceTicket.afficherListeTicketsRecherches(recherche);
        fireTableDataChanged();
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


