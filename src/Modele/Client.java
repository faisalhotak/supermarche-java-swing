package Modele;

import java.util.Date;

/**
 *
 * @author faisalhotak
 */

public class Client extends Personne {

    private int code;
    private Date date_inscription;
    private boolean client_fidele;

    public Client(int c, String n, String pr, String ru, String cp, String l, String pa, String nt, String nm, String email, Date di, boolean fidele) {
        super(n, pr, ru, cp, l, pa, nt, nm, email);
        this.code = c;
        this.date_inscription = di;
        this.client_fidele = fidele;
    }
    
    // Constructeur vide
    public Client(){}

    public Date getDate_inscription() {
        return date_inscription;
    }

    public void setDate_inscription(Date date_inscription) {
        this.date_inscription = date_inscription;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isClient_fidele() {
        return client_fidele;
    }

    public void setClient_fidele(boolean client_fidele) {
        this.client_fidele = client_fidele;
    }
}
