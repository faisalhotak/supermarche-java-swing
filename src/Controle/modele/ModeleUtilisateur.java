package Controle.modele;

import EncryptionDecryption.AES;
import Modele.ListeDesUtilisateurs;
import javax.swing.table.AbstractTableModel;
import Modele.Utilisateur;
import java.util.ArrayList;

public class ModeleUtilisateur extends AbstractTableModel{
    // pour récupérer les enreg et les mettre dans le modèle
    ListeDesUtilisateurs instanceUtilisateur = new ListeDesUtilisateurs();
    private ArrayList<Utilisateur> lesDonnees = instanceUtilisateur.getListeUtilisateurs();

    private final String[] lesTitres = {"Id", "Utilisateur", "Mot De Passe", "Droits", "Nom", "Prenom"};
    
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
            case 1 : return lesDonnees.get(rowIndex).getNomUtilisateur();
            case 2 : return lesDonnees.get(rowIndex).getMdp();
            case 3 : return lesDonnees.get(rowIndex).getDroits();
            case 4 : return lesDonnees.get(rowIndex).getlEmploye().getNom();
            case 5 : return lesDonnees.get(rowIndex).getlEmploye().getPrenom();
            default : return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex){
        return lesTitres[columnIndex];
    }

    public boolean creerMOD (Utilisateur lUtilisateur){
        boolean insert;
        
        insert = instanceUtilisateur.creerCRUD(lUtilisateur);
        
        if (insert) {            
            lesDonnees = instanceUtilisateur.getListeUtilisateursTriee();
            fireTableDataChanged();
        }
        return insert;
    }
    
    public boolean supprimerMOD(int indexRow, String vCode){
        boolean remove;
        remove = instanceUtilisateur.supprimerCRUD(vCode);
        
        if (remove){
            lesDonnees.remove(indexRow);
            this.fireTableRowsDeleted(indexRow, indexRow);
        }
        return remove;
    }
    
    public boolean modifierMOD(int firstRow, int lastRow, Utilisateur lUtilisateur){
        boolean modified;
        
        modified = instanceUtilisateur.modifierCRUD(lUtilisateur);
        
        if (modified){         
            lesDonnees.set(firstRow, lUtilisateur);
            fireTableRowsUpdated(firstRow, lastRow); 
        }
        return modified;
    }

    public ListeDesUtilisateurs getInstanceUtilisateur() {
        return instanceUtilisateur;
    }

    public void setInstanceUtilisateur(ListeDesUtilisateurs instanceUtilisateur) {
        this.instanceUtilisateur = instanceUtilisateur;
    }

    public ArrayList<Utilisateur> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<Utilisateur> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }

    public void mdpVisibles(boolean b){
        if (b) {
            for (Utilisateur util : lesDonnees) {
                String mdpCrypter = util.getMdp();
                String mdpDecrypter = AES.decrypt(mdpCrypter);
                util.setMdp(mdpDecrypter);
                this.fireTableDataChanged();
            }
        }
        else {
            for (Utilisateur util : lesDonnees) {
                String mdpDecrypter = util.getMdp();
                String mdpCrypter = AES.encrypt(mdpDecrypter);
                util.setMdp(mdpCrypter);
                this.fireTableDataChanged();
            }
        }
        
    }
    
    // permet de mettre à jour le modèle
    // suite à de nouvelles recherches
    // et d'informer les vues affichant ce modèle
    public void lireRecupMOD(ArrayList<Utilisateur> nouvellesDonnees){
        lesDonnees = nouvellesDonnees;
         fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex){
        switch (columnIndex){
            default : return Object.class;
        }
    }   
}


