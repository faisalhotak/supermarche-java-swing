package Controle.modele;


import Modele.Poste;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

public class ModelePoste extends DefaultComboBoxModel{
    // pour récupérer les enreg et les mettre dans le modèle
    Poste instancePoste = new Poste();
    private ArrayList<Poste> lesDonnees = instancePoste.getLesEnreg();
    
    protected ArrayList<Poste> getStrings(){
        return lesDonnees;
    }

    public Object getSelectedString(){
        return getSelectedItem();
    }
    
    @Override
    public Poste getElementAt (int index){
        return lesDonnees.get(index);
    }
    
    @Override
    public int getSize(){
        return lesDonnees.size();
    }
    
    @Override
    public int getIndexOf(Object element){
        return lesDonnees.indexOf(element);
    }

    public Poste getInstancePoste() {
        return instancePoste;
    }

    public void setInstancePoste(Poste instancePoste) {
        this.instancePoste = instancePoste;
    }

    public ArrayList<Poste> getLesDonnees() {
        return lesDonnees;
    }

    public void setLesDonnees(ArrayList<Poste> lesDonnees) {
        this.lesDonnees = lesDonnees;
    }
    
    public int getIdByDesignation(String designation){        
        for (Poste lePoste : lesDonnees) {
            if (lePoste.getDesignation().equals(designation)){
                return lePoste.getId();
            }
        }
        return -1;
    }
}