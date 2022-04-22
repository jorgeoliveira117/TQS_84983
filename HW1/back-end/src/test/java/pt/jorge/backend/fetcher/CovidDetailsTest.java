package pt.jorge.backend.fetcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.jorge.backend.entities.CovidDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CovidDetailsTest {

    List<CovidDetails> detailsList;

    @BeforeEach
    void setUp() {
        detailsList = new ArrayList<CovidDetails>();
    }

    @Test
    @DisplayName("Given a List of CovidDetails with only duplicated days the fetcher returns only one entry, with the most recent time")
    void testReduceListWithSameDay() {
        String correctDetails = "This is the one";
        String incorrectDetails = "This is NOT the one";
        CovidDetails cdTemp = new CovidDetails();
        cdTemp.setCountry(incorrectDetails);
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setCountry(incorrectDetails);
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setCountry(incorrectDetails);
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setCountry(correctDetails);
        cdTemp.setDay(Calendar.getInstance());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        cdTemp.setTime(cal);
        detailsList.add(cdTemp);

        List<CovidDetails> returnedList = CovidDetails.reduce(detailsList);

        // Make sure it only returns one entry
        assertEquals(1, returnedList.size());
        // Check if the correct entry is returned, this is, the most recent entry
        assertEquals(correctDetails, returnedList.get(0).getCountry());

    }

    @Test
    @DisplayName("Given a List of CovidDetails with different and duplicated days the fetcher returns only one entry for each day")
    void testReduceListWithDifferentDays() {
        Calendar today = Calendar.getInstance();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        CovidDetails cdTemp = new CovidDetails();

        cdTemp.setDay(today);
        cdTemp.setTime(today);
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setDay(today);
        cdTemp.setTime(today);
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setDay(tomorrow);
        cdTemp.setTime(tomorrow);
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setDay(tomorrow);
        cdTemp.setTime(tomorrow);
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setDay(yesterday);
        cdTemp.setTime(yesterday);
        detailsList.add(cdTemp);
        cdTemp = new CovidDetails();
        cdTemp.setDay(yesterday);
        cdTemp.setTime(yesterday);
        detailsList.add(cdTemp);

        List<CovidDetails> returnedList = CovidDetails.reduce(detailsList);

        // Make sure it only returns three entries
        assertEquals(3, returnedList.size());
        // Check that none of the entries have the same day
        assertNotEquals(returnedList.get(0).getTime(), returnedList.get(1).getTime());
        assertNotEquals(returnedList.get(1).getTime(), returnedList.get(2).getTime());
        assertNotEquals(returnedList.get(0).getTime(), returnedList.get(2).getTime());

    }


    @Test
    @DisplayName("Given several strings of numbers, test if the expected results are correct")
    void testParseNumber() {
        // Very usefull when using the API since for number fields it can return:
        // 1    "1"     '1'     <- For present values
        // null "null"          <- When a value is missing
        String nullString = null;
        String stringWithNull = "null";
        String invalidString = "not a number";
        String numberInsideQuotes = "'10'";
        String numberInsideQuotationMark = "\"10\"";
        String number = "10";
        int expected = 10;
        int invalid = -1;

        assertEquals(invalid, CovidDetails.parseNumber(nullString));
        assertEquals(invalid, CovidDetails.parseNumber(stringWithNull));
        assertEquals(invalid, CovidDetails.parseNumber(invalidString));
        assertEquals(expected, CovidDetails.parseNumber(numberInsideQuotes));
        assertEquals(expected, CovidDetails.parseNumber(numberInsideQuotationMark));
        assertEquals(expected, CovidDetails.parseNumber(number));

    }
}