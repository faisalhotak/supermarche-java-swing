
package Modele;

import EncryptionDecryption.AES;
import Fichier.Fichier;
import java.util.Objects;

/**
 *
 * @author faisalhotak
 */
public class Utilisateur {
    private int id;
    private int droits;
    private String nomUtilisateur;
    private String mdp;
    private Employe lEmploye;
    
    public Utilisateur(int i, String n, String m, int r, Employe lEmploye){
        this.id = i;
        this.nomUtilisateur = n;
        this.mdp = m;
        this.droits = r;
        this.lEmploye = lEmploye;
    }
    
    // Constructeur vide
    public Utilisateur(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
    
    public int getDroits() {
        return droits;
    }

    public void setDroits(int droits) {
        this.droits = droits;
    }

    public Employe getlEmploye() {
        return lEmploye;
    }

    public void setlEmploye(Employe lEmploye) {
        this.lEmploye = lEmploye;
    }     
}
