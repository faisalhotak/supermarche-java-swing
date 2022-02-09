package Controle.modele;


import Modele.Localite;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

public class ModeleLocalite extends DefaultComboBoxModel{
    // pour récupérer les enreg et les mettre dans le modèle
    Localite instanceLocalite = new Localite();
    private ArrayList<Localite> lesDonnees = instanceLocalite.getLesEnreg();
    
    protected ArrayList<Localite> getStrings(){
        return lesDonnees;
    }
    
    public Object getSelectedString(){
        return getSelectedItem();
    }
    
    @Override
    public Localite getElementAt (int index){
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