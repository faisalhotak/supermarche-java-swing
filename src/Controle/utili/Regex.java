package Controle.utili;

public class Regex {
    public static boolean estUnNom(String nom){
        String expression = "^[a-zA-Z]*$"; 
        return nom.matches(expression);
    }
    
    public static boolean estUnNumeroDeTelephone(String numero){
        String expression = "^\\d+(\\.\\d+)*$";
        return numero.matches(expression);
    }
    
    public static boolean estUnCodeBarres(String code){
        String expression = "[0-9]+";
        return code.matches(expression);
    }
    
    public static boolean estUneQuantite(String quantite){
        String expression = "[0-9]+";
        return quantite.matches(expression);
    }
    
    public static boolean estUneDecimale(String decimale){
        String expression = "?<=^| )\\d+(\\.\\d+)?(?=$| ";
        return decimale.matches(expression);
    }         
}
