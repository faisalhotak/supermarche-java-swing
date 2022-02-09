package Modele;

import java.io.Serializable;

/**
 *
 * @author faisalhotak
 */

public class Adresse {

    protected String rue, code_postal, localite, pays;
    
    public Adresse(String ru, String cp, String lo, String pa){
        this.rue = ru;
        this.code_postal = cp;
        this.localite = lo;
        this.pays = pa;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(String code_postal) {
        this.code_postal = code_postal;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }
    
    @Override
    public String toString(){
        return "Rue : " + this.rue + "\nCode postal : " + this.code_postal + "\nLocalité : " + this.localite + "\n";
    }
    
}
