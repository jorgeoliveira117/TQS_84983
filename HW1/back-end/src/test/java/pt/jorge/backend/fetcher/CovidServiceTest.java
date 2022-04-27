package pt.jorge.backend.fetcher;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.CovidDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CovidServiceTest {

    private String TODAY_URL = "https://covid-193.p.rapidapi.com/statistics";


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    List<CovidDetails> detailsList;
    CovidDetails cdTemp;

    @Mock
    private CovidApiFetcher fetcher;

    @InjectMocks
    private CovidService service;

    @BeforeEach
    void setUp() {

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Portugal");
        cdTemp.setContinent("Europe");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        cdTemp.setNewCases(100);

        detailsList = new ArrayList<>();
        detailsList.add(cdTemp);

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Europe");
        cdTemp.setContinent("Europe");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        cdTemp.setNewCases(20000);

        detailsList.add(cdTemp);

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Asia");
        cdTemp.setContinent("Asia");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        cdTemp.setNewCases(100000);

        detailsList.add(cdTemp);

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Japan");
        cdTemp.setContinent("Asia");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        cdTemp.setNewCases(20000);

        detailsList.add(cdTemp);

        cdTemp = new CovidDetails();
        cdTemp.setCountry("Spain");
        cdTemp.setContinent("Europe");
        cdTemp.setDay(Calendar.getInstance());
        cdTemp.setTime(Calendar.getInstance());
        cdTemp.setNewCases(10000);

        detailsList.add(cdTemp);
        //Mockito.when(fetcher.getFromURL(TODAY_URL)).thenReturn(detailsList);
        Mockito.when(fetcher.getFromURL(any())).thenReturn(detailsList);

    }
    // ==================================
    // public CovidDetails getToday(String country)
    @Test
    public void whenGetTodayForValidCountry_GetResults(){
        String country = cdTemp.getCountry();

        CovidDetails ret = service.getToday(country);
        assertEquals(country, ret.getCountry());
    }
    @Test
    public void whenGetTodayForInvalidCountry_GetNull(){
        String country = "portugaaaaaal";

        assertNull(service.getToday(country));
    }
    // ==================================
    // public List<CovidDetails> getToday()
    @Test
    public void whenGetToday_GetSeveralResults(){

        List<CovidDetails> ret = service.getToday();
        assertEquals(detailsList.size(), ret.size());
    }
    // ==================================
    // public List<CovidDetails> getContinents()
    @Test
    public void whenGetContinents_GetContinents(){

        List<CovidDetails> ret = service.getContinents();

        for(CovidDetails val: ret){
            assertEquals(val.getCountry(), val.getContinent());
        }
    }
    // ==================================
    // public List<CovidDetails> getTop(int n)
    @Test
    public void whenGetTopN_GetNEntries(){

        int n = 2;
        List<CovidDetails> ret = service.getTop(n);

        assertEquals(n, ret.size());
        for(int i = 1; i < ret.size(); i++){
            assertTrue(ret.get(i-1).getNewCases() > ret.get(i).getNewCases());
        }
    }
    // ==================================
    // public List<CovidDetails> getHistory(String country)
    @Test
    public void whenGetHistory_GetDistinctDays(){
        String country = "Portugal";
        Calendar date = Calendar.getInstance();
        List<CovidDetails> allHistoricalData = new ArrayList<>();

        {
        // add 5 entries for 3 days
        // day 1
        //      mostRecent
        CovidDetails day1MostRecent = new CovidDetails();
        day1MostRecent.setCountry(country);
        day1MostRecent.setDay(date);
        day1MostRecent.setTime(date);
        allHistoricalData.add(day1MostRecent);
        //      Oldest
        date = Calendar.getInstance();
        date.add(Calendar.MINUTE, -1);
        CovidDetails day1Oldest = new CovidDetails();
        day1Oldest.setCountry(country);
        day1Oldest.setDay(date);
        day1Oldest.setTime(date);
        allHistoricalData.add(day1Oldest);
        // day 2
        date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, -1);
        CovidDetails day2 = new CovidDetails();
        day2.setCountry(country);
        day2.setDay(date);
        day2.setTime(date);
        allHistoricalData.add(day2);
        // day 3
        //      mostRecent
        date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, -2);
        CovidDetails day3MostRecent = new CovidDetails();
        day3MostRecent.setCountry(country);
        day3MostRecent.setDay(date);
        day3MostRecent.setTime(date);
        allHistoricalData.add(day3MostRecent);
        //      Oldest
        date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, -2);
        date.add(Calendar.MINUTE, -1);
        CovidDetails day3Oldest = new CovidDetails();
        day3Oldest.setCountry(country);
        day3Oldest.setDay(date);
        day3Oldest.setTime(date);
        allHistoricalData.add(day3Oldest);

        }

        int expectedDays = 3;
        Mockito.when(fetcher.getFromURL(any())).thenReturn(allHistoricalData);
        List<CovidDetails> ret = service.getHistory(country);
        assertEquals(expectedDays, ret.size());
    }
    // ==================================
    // public List<CovidDetails> getHistory(String country, Calendar day)
    @Test
    public void whenGetHistory_GetSeveralEntries(){
        String country = "Portugal";
        Calendar date = Calendar.getInstance();
        List<CovidDetails> allHistoricalData = new ArrayList<>();

        {
            CovidDetails day1MostRecent = new CovidDetails();
            day1MostRecent.setCountry(country);
            day1MostRecent.setDay(date);
            day1MostRecent.setTime(date);
            allHistoricalData.add(day1MostRecent);

            CovidDetails day1Middle = new CovidDetails();
            day1Middle.setCountry(country);
            day1Middle.setDay(date);
            day1Middle.setTime(date);
            allHistoricalData.add(day1Middle);

            CovidDetails day1Oldest = new CovidDetails();
            day1Oldest.setCountry(country);
            day1Oldest.setDay(date);
            day1Oldest.setTime(date);
            allHistoricalData.add(day1Oldest);
        }

        int expectedEntries = 3;
        Mockito.when(fetcher.getFromURL(any())).thenReturn(allHistoricalData);

        List<CovidDetails> ret = service.getHistory(country, date);
        assertEquals(expectedEntries, ret.size());
    }
    // ==================================
    // public CovidDetails getHistorySingle(String country, Calendar day)
    @Test
    public void whenGetHistory_GetOneEntry(){
        String country = "Portugal";
        Calendar date = Calendar.getInstance();
        List<CovidDetails> allHistoricalData = new ArrayList<>();

        {
            CovidDetails day1MostRecent = new CovidDetails();
            day1MostRecent.setCountry(country);
            day1MostRecent.setDay(date);
            day1MostRecent.setTime(date);
            allHistoricalData.add(day1MostRecent);

            date = Calendar.getInstance();
            date.add(Calendar.MINUTE, -1);
            CovidDetails day1Middle = new CovidDetails();
            day1Middle.setCountry(country);
            day1Middle.setDay(date);
            day1Middle.setTime(date);
            allHistoricalData.add(day1Middle);

            date = Calendar.getInstance();
            date.add(Calendar.MINUTE, -2);
            CovidDetails day1Oldest = new CovidDetails();
            day1Oldest.setCountry(country);
            day1Oldest.setDay(date);
            day1Oldest.setTime(date);
            allHistoricalData.add(day1Oldest);
        }

        Mockito.when(fetcher.getFromURL(any())).thenReturn(allHistoricalData);

        CovidDetails ret = service.getHistorySingle(country, date);
        assertNotNull(ret);
    }

}
