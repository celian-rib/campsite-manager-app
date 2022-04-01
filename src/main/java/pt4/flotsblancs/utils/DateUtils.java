package pt4.flotsblancs.utils;

import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * @return la date donnée convertie en LocalDate
     */
    public static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @return la LocalDate donnée convertit en Date
     */
    public static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @return la date donnée convertie en String
     */
    public static String toFormattedString(Date date) {
        return toLocale(date).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    /**
     * @return vrai si la date est dans le passé
     */
    public static boolean isInPast(Date date) {
        return toLocale(date).isBefore(LocalDate.now());
    }

    /**
     * @return vrai si la date est dans le futur
     */
    public static boolean isAfter(Date date, Date comparedWith) {
        return toLocale(date).isAfter(toLocale(comparedWith));
    }

    /**
     * @return la date avec un nombre de jours ajoutés à la date donnée
     */
    public static Date plusDays(Date date, int days) {
        return DateUtils.fromLocale(DateUtils.toLocale(date).plusDays(days));
    }

    /**
     * @return la date avec un nombre de jours retirés à la date donnée
     */
    public static Date minusDays(Date date, int days) {
        return DateUtils.fromLocale(DateUtils.toLocale(date).minusDays(days));
    }
}
