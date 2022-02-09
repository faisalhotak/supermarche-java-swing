package EncryptionDecryption;

import Fichier.Fichier;

/**
 *
 * @author faisalhotak
 */

public class TestAES {

    public static void main(String[] args) {
        
        String encryptedString, decryptedString;
        final String clefSecrete = lectureClefSecrete();

        String chaine = "hotakfaisal@gmail.com";  
              
        encryptedString = AES.encrypt(chaine);    
        decryptedString = AES.decrypt(encryptedString);
        
        System.out.println("Voici la clef : " + clefSecrete);
        System.out.println("Voici la chaîne de base : " + chaine);
        System.out.println("Voici la chaîne cryptée : " + encryptedString);
        System.out.println("Voici la chaîne décryptée : " + decryptedString);

    }
    
    private static String lectureClefSecrete(){
        String clef;
        
        clef = Fichier.lire("files/clef.txt");       
        return clef;
    }
}

