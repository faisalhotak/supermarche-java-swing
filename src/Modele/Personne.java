package Modele;

import java.io.Serializable;

/**
 *
 * @author faisalhotak
 */

public class Personne {

    protected String nom, prenom, num_telephone, num_mobile, email;
    protected Adresse adresse;
    
    public Personne (String n, String pr, String ru, String cp, String l, String pa, String nt, String nm, String em) {       
        this.nom = n;
        this.prenom = pr;       
        this.num_telephone = nt;
        this.email = em;
        this.adresse = new Adresse(ru, cp, l, pa);
    }
    
    // Constructeur vide
    public Personne(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNum_telephone() {
        return num_telephone;
    }

    public void setNum_telephone(String num_telephone) {
        this.num_telephone = num_telephone;
    }

    public String getNum_mobile() {
        return num_mobile;
    }

    public void setNum_mobile(String num_mobile) {
        this.num_mobile = num_mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}