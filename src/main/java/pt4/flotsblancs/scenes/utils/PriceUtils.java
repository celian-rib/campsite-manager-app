package pt4.flotsblancs.scenes.utils;

public class PriceUtils {
    /**
     * transforme un prix donné dans le bon format String
     * 
     * @param price
     * @return
     */
    public static String priceToString(int price){
        float res = price/10;
        if(res % 1==0){
            return res/10+"0";
        }
        return res/10+"";
    }
}
