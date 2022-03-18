package pt4.flotsblancs.utils;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String toFormattedString(Date date) {
        return toLocale(date).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public static boolean isInPast(Date date) {
        return toLocale(date).isBefore(LocalDate.now());
    }
    
    public static boolean isAfter(Date date, Date comparedWith) {
        return toLocale(date).isAfter(toLocale(comparedWith));
    }
    
    public static Date plusDays(Date date, int days) {
        return DateUtils.fromLocale(DateUtils.toLocale(new Date()).plusDays(days));
    }
}
