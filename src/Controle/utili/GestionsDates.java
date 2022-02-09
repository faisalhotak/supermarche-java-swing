package Controle.utili;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GestionsDates {
    public static String dateEnChaineFR(Date laDate) {
        // choix de la langue
        Locale locale = Locale.FRANCE;
        // définition du format à utiliser
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        String date = dateFormat.format(laDate);
        return date;
    }
    
    public static String chaineDateFRversEN(String dateFR) {
        String dateEN="";
        String vAnneeEN = dateFR.substring(6,10);
        String vMoisEN = dateFR.substring(3,5);
        String vJourEN = dateFR.substring(0,2);
        
        dateEN = vAnneeEN + "-" + vMoisEN + "-" + vJourEN;

        return dateEN;
    }
    
     public static Date dateJavaEnDateSQL(Date laDate) {
        Date dateSQL = new java.sql.Date(laDate.getTime());
        System.out.println(dateSQL);
        return dateSQL;
    }
    
    public static String chaineDateENversFR(String dateEN) {
        String dateFR="";
        String vJourFR = dateEN.substring(8,10);
        String vMoisFR = dateEN.substring(5,7);
        String vAnneeFR = dateEN.substring(0,4);
        
        dateFR = vJourFR + "/" + vMoisFR + "/" + vAnneeFR;
        return dateFR;
    }
    
    public static Date chaineENversDateJava(String laDateChaineEN) throws ParseException {

        SimpleDateFormat dateStandard = new SimpleDateFormat("yyyy-MM-dd");
        
        Date laDateDate= dateStandard.parse(laDateChaineEN);

        return laDateDate;
    }
    
}
