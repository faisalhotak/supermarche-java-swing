package Controle;

import EncryptionDecryption.AES;
import Modele.ListeDesUtilisateurs;
import Modele.Parametres;
import Modele.Utilisateur;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ControleConnexionSingleton {
    private static Parametres  lesParametres;
    private static Connection connect = null;
    private volatile static ControleConnexionSingleton unique = null;
    private static boolean etatConnexion = false;
    private static Utilisateur user;
    
    // Le code suivant dans le constructeur privé permet de 
    // garantir que plusieurs threads gèrent correctement l'attribut unique ...
    private ControleConnexionSingleton() {
        Connexion();
    }
    
    private void Connexion(){ 
        boolean ok = true;
        lesParametres = new Parametres();
        try {
            Class.forName(lesParametres.getDriverSGBD());//la méthode forName retourne le nom de la classe du pilote
            etatConnexion = true;
            
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(ControleConnexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                    "Classe non trouvée"
                    + " pour le chargement "
                    + "du pilote JDBC MySQL",
                    "ALERTE", JOptionPane.ERROR_MESSAGE);
            ok = false;
            etatConnexion = false;
        }
        // Etablissement de la connexion
        //-------------------------------
        if (ok == true) {
                    
            try {
                this.connect = DriverManager.getConnection(lesParametres.getServeurBD(), 
                        lesParametres.getNomUtilisateur(), lesParametres.getMotDePasse());

                etatConnexion = true;

            } catch (SQLException e) {
                
                if (e.getErrorCode() == 1045) {
                    JOptionPane.showMessageDialog(null, "Les identifiants pour "
                            + "se connecter à la base de données sont incorrects !", "Alerte", JOptionPane.ERROR_MESSAGE);
                }
                
                JOptionPane op = new JOptionPane();
                op.showMessageDialog(null, "Impossible de se connecter"
                            + " à la base de données", "Alerte", JOptionPane.ERROR_MESSAGE);

                etatConnexion = false;
            }            
        }
    }

    // Cette méthode va nous retourner l'instance existante 
    // ou la créer si elle n'existe pas
    public static ControleConnexionSingleton getInstance() {
        if (unique == null) {
            synchronized (ControleConnexionSingleton.class) {
                if (connect == null) {
                    new ControleConnexionSingleton();
                }
            }
        }

        return unique;
    }

    public static Connection getConnect() {
        return connect;
    }    
    
    public static boolean isEtatConnexion() {
        return etatConnexion;
    }
    
    //fermeture de la connexion
    public static void fermetureConnect() {
        try {
            if(etatConnexion) connect.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Problème rencontré"
                    + "à la fermeture de la connexion",
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static boolean controle(String Nom, String MotDePasse) {

        String MotDePasseCrypte = AES.encrypt(MotDePasse);
        
        ListeDesUtilisateurs instanceUtilisateur = new ListeDesUtilisateurs();
        Utilisateur enteredUser = instanceUtilisateur.chercherCRUD(Nom, MotDePasseCrypte);

        if (enteredUser != null){
            user = enteredUser;
            return true;
        } 

        return false;
    }

    public static Utilisateur getUser() {
        return user;
    }

    public static Parametres getLesParametres() {
        return lesParametres;
    }
}

