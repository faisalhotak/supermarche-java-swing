package Modele;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

/**
 *
 * @author faisalhotak
 */

public class Paiement {

    private int id;
    private TicketCaisse ticket_caisse;
    private ModePaiement mode_paiement;
    private BigDecimal montant;
    private Timestamp date_paiement;
    
    public Paiement(int id, TicketCaisse idtc, ModePaiement mdp, Timestamp dpm, BigDecimal mt){
        this.id = id;
        this.ticket_caisse = idtc;
        this.mode_paiement = mdp;
        this.date_paiement = dpm;
        this.montant = mt;     
    }
    
    // Constructeur vide
    public Paiement(){};

    public TicketCaisse getTicket_caisse() {
        return ticket_caisse;
    }

    public void setTicket_caisse(TicketCaisse ticket_caisse) {
        this.ticket_caisse = ticket_caisse;
    }

    public ModePaiement getMode_paiement() {
        return mode_paiement;
    }

    public void setMode_paiement(ModePaiement mode_paiement) {
        this.mode_paiement = mode_paiement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Timestamp getDate_paiement() {
        return date_paiement;
    }

    public void setDate_paiement(Timestamp date_paiement) {
        this.date_paiement = date_paiement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
