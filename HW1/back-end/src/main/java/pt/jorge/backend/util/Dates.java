package pt.jorge.backend.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Dates {

    private Dates(){

    }
    public static String countryAndDate(String country, Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return country.toLowerCase() + "-" + sdf.format(date.getTime());
    }

    public static String countryAndDate(String country, String date){
        return country.toLowerCase() + "-" + date;
    }

    // Same functionality as DateUtils
    // https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/time/DateUtils.html#isSameDay(java.util.Calendar,%20java.util.Calendar)

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }
}
