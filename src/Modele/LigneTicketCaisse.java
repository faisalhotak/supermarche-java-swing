package Modele;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author faisalhotak
 */

public class LigneTicketCaisse {

    private Produit leProduit;
    private int id, id_ticket_caisse, quantiteAchetee;
    private BigDecimal prixUnitaire;
    
    public LigneTicketCaisse(int id, int idtc, Produit p, BigDecimal pu, int q){
        this.id = id;
        this.id_ticket_caisse = idtc;
        this.leProduit = p;
        this.prixUnitaire = pu.setScale(2, RoundingMode.HALF_EVEN);
        this.quantiteAchetee = q;      
    }
    
    // Constructeur vide
    public LigneTicketCaisse(){};

    public int getQuantiteAchetee() {
        return quantiteAchetee;
    }

    public void setQuantiteAchetee(int quantite) {
        this.quantiteAchetee = quantite;
    }

    public Produit getLeProduit() {
        return leProduit;
    }

    public void setLeProduit(Produit leProduit) {
        this.leProduit = leProduit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getId_ticket_caisse() {
        return id_ticket_caisse;
    }

    public void setId_ticket_caisse(int id_ticket_caisse) {
        this.id_ticket_caisse = id_ticket_caisse;
    }
    
    public void diminuerQuantite(int quantite){
        this.quantiteAchetee -= quantite;
    }
}
