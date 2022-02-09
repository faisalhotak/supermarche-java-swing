package Controle.modele;


import Modele.Pays;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

public class ModelePays extends DefaultComboBoxModel{
    // pour récupérer les enreg et les mettre dans le modèle
    Pays instancePays = new Pays();
    private ArrayList<Pays> lesDonnees = instancePays.getLesEnreg();
    
    protected ArrayList<Pays> getStrings(){
        return lesDonnees;
    }
    
    public Object getSelectedString(){
        return getSelectedItem();
    }
    
    @Override
    public Pays getElementAt (int index){
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