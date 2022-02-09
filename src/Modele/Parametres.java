/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import EncryptionDecryption.AES;
import Fichier.Fichier;

public class Parametres {
    private String nomUtilisateur;
    private String motDePasse;
    private String serveurBD;
    private String driverSGBD;

    public Parametres() {
        String[] donneesParametres;
        donneesParametres = lectureFichierParametres();
        
        nomUtilisateur = donneesParametres[0];
        motDePasse = donneesParametres[1];
        driverSGBD = donneesParametres[2];
        serveurBD = donneesParametres[3];       
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getServeurBD() {
        return serveurBD;
    }

    public void setServeurBD(String serveurBD) {
        this.serveurBD = serveurBD;
    }

    public String getDriverSGBD() {
        return driverSGBD;
    }

    public void setDriverSGBD(String driverSGBD) {
        this.driverSGBD = driverSGBD;
    }
    
    // Lecture du fichier de paramètres permettant de se connecter à la BD
    private String[] lectureFichierParametres(){
        String donneesLues, decryptedString;
        String[] split;
        
        donneesLues = Fichier.lire("files/parametres.enc");
        decryptedString = AES.decrypt(donneesLues);
        split = decryptedString.split("\n");
        
        return split;
    }
}
