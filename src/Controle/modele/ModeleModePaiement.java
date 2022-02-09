package Controle.modele;

import Modele.ModePaiement;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

public class ModeleModePaiement extends DefaultComboBoxModel{
    // pour récupérer les enreg et les mettre dans le modèle
    ModePaiement instanceModePaiement = new ModePaiement();
    private ArrayList<ModePaiement> lesDonnees = instanceModePaiement.getLesEnreg();
    
    protected ArrayList<ModePaiement> getStrings(){
        return lesDonnees;
    }
    
    public Object getSelectedString(){
        return getSelectedItem();
    }
    
    @Override
    public ModePaiement getElementAt (int index){
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
}