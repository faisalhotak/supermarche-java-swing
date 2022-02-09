package Modele;

import java.util.Date;
import java.util.Objects;

public class Employe extends Personne {
    
    private int code;
    private Poste poste;
    private double salaire;
    private Date date_embauche;

    public Employe(int c, String n, String pr, String ru, String cp, String l, String pa, String nt, String nm, String email, Date di, Poste po, double s) {
        super(n, pr, ru, cp, l, pa, nt, nm, email);                
        this.code = c;
        this.poste = po;       
        this.salaire = s;
        this.date_embauche = di;
    }
    
    // Constructeur vide
    public Employe(){}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Date getDate_embauche() {
        return date_embauche;
    }

    public void setDate_embauche(Date date_embauche) {
        this.date_embauche = date_embauche;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.code;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Employe other = (Employe) obj;
        if (this.code != other.code) {
            return false;
        }
        return true;
    }
}
