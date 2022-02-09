package Fichier;

import EncryptionDecryption.AES;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Fichier {
    
    public static String lire (String eFilePath){
        try{         
            File inputFile = new File(eFilePath);
            FileInputStream inputStream = new FileInputStream(inputFile);

            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);     
            
            String retrieved = new String(inputBytes, "UTF-8");
            
            inputStream.close();
            return retrieved;
        }
        catch (FileNotFoundException e){
            String erreur = " ";
            if (eFilePath.equals("files/parametres.enc")) erreur = " 'parametres.enc' dans le dossier 'files' ";
            JOptionPane.showMessageDialog(null, "Le fichier" + erreur +"n'existe pas !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Utilisation des paramètres par défaut !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
            return initDefaultParameters();
        }
        catch (IOException e){
            String erreur = " ";
            if (eFilePath.equals("files/parametres.enc")) erreur = " 'parametres.enc' dans le dossier 'files' ";
            JOptionPane.showMessageDialog(null, "Impossible de lire le fichier" + erreur + "!", "Erreur", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Utilisation des paramètres par défaut !", "Erreur", JOptionPane.INFORMATION_MESSAGE);
            return initDefaultParameters();
        }
    }
    
    public static void ecrire (String eString, String eFilePath){
        try{
            FileOutputStream outputStream = new FileOutputStream(eFilePath);
            outputStream.write(eString.getBytes("UTF-8"));
            outputStream.close();
        }
        catch (IOException e){
            String erreur = " ";
            if (eFilePath.equals("files/parametres.enc")) erreur = " 'parametres.enc' dans le dossier 'files' ";
            JOptionPane.showMessageDialog(null, "Impossible d'écrire dans le fichier" + erreur + "!", "Erreur", JOptionPane.INFORMATION_MESSAGE);
        }     
    }
    
    private static String initDefaultParameters(){
        String root = "root";
        String mdp = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        String db = "jdbc:mysql://localhost:3306/Supermarche";
        
        String parametresDefaut = AES.encrypt(root + "\n" + mdp + "\n" 
                + driver + "\n" + db);
        
        ecrire(parametresDefaut, "files/parametres.enc");
        
        return parametresDefaut;
    }
}
