package pt4.flotsblancs.scenes.utils;

public class PriceUtils {
    /**
     * transforme un prix donné en entier dans le bon format String
     * 
     * @param price prix sous forme d'entier (Ex: 128)
     * @return string représentant le prix (Ex: "1.28")
     */
    public static String priceToString(int price){
        float res = price/10;
        if(res % 1==0){
            return res/10+"0";
        }
        return res/10+"";
    }
}
