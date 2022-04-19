package pt.jorge.backend.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class StringConverterTest {

    @Test
    @DisplayName("The string displayed follows the correct format")
    void countryAndDateToday() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // Simple test for today's date
        String expectedResult = "portugal-" + sdf.format(cal.getTime());
        Calendar today = Calendar.getInstance();
        String countryDate = StringConverter.countryAndDate("portugal", today);
        assertEquals(expectedResult, countryDate);
    }

    @Test
    @DisplayName("The result comes in lowercase for the given date")
    void countryAndDateLowercase() {
        // Simple test for today's date
        String expectedResult = "portugal-2020-04-01";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String countryDate = StringConverter.countryAndDate("PORTUgal", cal);
        assertEquals(expectedResult, countryDate);
    }
}