package pt4.flotsblancs.scenes.utils;

import java.util.regex.Pattern;

public class TxtFieldValidation {
    
    /**
     * renvoie vrai si l'email d'un client est valide
     * 
     * @param emailString
     * @return
     */
    public static boolean emailValidation(String emailString){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");//source : https://regexr.com/3e48o
        return pattern.matcher(emailString).matches();
    }

    /**
     * renvoie vrai si le téléphone d'un client est valide
     * @param phoneString
     * @return
     */
    public static boolean phoneValidation(String phoneString){
        Pattern pattern = Pattern.compile("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");//source : https://stackoverflow.com/questions/38483885/regex-for-french-telephone-numbers
        return pattern.matcher(phoneString).matches();
    }
}
