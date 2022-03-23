package pt4.flotsblancs.scenes.utils;

import java.util.regex.Pattern;

public class TxtFieldValidation {
    
    public static boolean emailValidation(String emailString){
        Pattern pattern = Pattern.compile("/^\\S+@\\S+.\\S+$/");//source https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
        return pattern.matcher(emailString).matches();
    }

    public static boolean phoneValidation(String phoneString){
        Pattern pattern = Pattern.compile("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");//source : https://stackoverflow.com/questions/38483885/regex-for-french-telephone-numbers
        return pattern.matcher(phoneString).matches();
    }
}
