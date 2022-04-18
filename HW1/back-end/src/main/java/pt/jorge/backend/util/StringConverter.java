package pt.jorge.backend.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringConverter {

    public static String countryAndDate(String country, Calendar date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return country.toLowerCase() + "-" + sdf.format(date.getTime());
    }
}
