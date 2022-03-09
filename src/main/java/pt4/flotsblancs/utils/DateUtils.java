package pt4.flotsblancs.utils;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {
    public static LocalDate toLocale(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date fromLocale(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
