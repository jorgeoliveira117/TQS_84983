package pt.jorge.backend.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.entities.helper.CountryStatisticsResponse;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class CovidFetcher {

    private static final Logger log = LoggerFactory.getLogger(CovidFetcher.class);
    // Not the best aproach, but as the git repository is only shared with the teacher it should be 'safe';
    private static final String apiKey = "8b7adc7dd1mshc90568bcfe94194p19bf1ajsnb9339fef134f";

    private final RestTemplate restTemplate;
    // Used in every request
    private final HttpEntity<CountryStatisticsResponse> request;


    private final String todayURL = "https://covid-193.p.rapidapi.com/statistics";
    private final String historyURL = "https://covid-193.p.rapidapi.com/history";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public CovidFetcher(RestTemplateBuilder builder){
        restTemplate = builder.build();
        // Configure the headers for every request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-RapidAPI-Host", "covid-193.p.rapidapi.com");
        headers.set("X-RapidAPI-Key", apiKey);
        request = new HttpEntity<>(headers);
    }

    private List<CovidDetails> getFromURL(String url){
        log.info("Creating GET to " + url);
        ResponseEntity<CountryStatisticsResponse> stats = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                CountryStatisticsResponse.class
        );
        if(stats.getBody() == null){
            log.info("[" + stats.getStatusCodeValue() + "] - No content");
        }
        log.info("[" + stats.getStatusCodeValue() + "] - " + stats.getBody().getResponse().length + " results");
        CountryStatistic[] response = stats.getBody().getResponse();
        if(response.length == 0)
            return null;
        return convert(response);
    }

    /** Returns statistics for a country */
    public CovidDetails getToday(String country){
        String url = todayURL + "?country=" + country;
        List<CovidDetails> stats = getFromURL(url);
        if(stats == null || stats.size() == 0)
            return null;
        return stats.get(0);
    }
    /** Returns statistics from every country */
    public List<CovidDetails> getToday(){
        List<CovidDetails> stats = getFromURL(todayURL);
        if(stats == null || stats.size() == 0)
            return null;
        return stats;
    }
    /** Returns every daily statistics from a given country */
    public List<CovidDetails> getHistory(String country){
        String url = historyURL + "?country=" + country;
        List<CovidDetails> stats = getFromURL(url);
        if(stats == null || stats.size() == 0)
            return null;
        return reduce(stats);
    }
    /** Returns every statistics from a given country on a give day*/
    public List<CovidDetails> getHistory(String country, Calendar day){
        String url = historyURL + "?country=" + country + "&day=" + sdf.format(day.getTime());
        List<CovidDetails> stats = getFromURL(url);
        if(stats == null || stats.size() == 0)
            return null;
        return stats;
    }

    /** Converts a CountryStatistic object to CovidDetails object */
    public CovidDetails convert(CountryStatistic cs){
        CovidDetails cd = new CovidDetails();
        cd.setCountry(cs.getCountry());
        cd.setContinent(cs.getContinent());
        cd.setDay(cs.getDay());
        cd.setTime(cs.getTime());
        cd.setPopulation(cs.getPopulation());
        cd.setNewCases(parseNumber(cs.getCases().getNewCases()));
        cd.setActiveCases(parseNumber(cs.getCases().getActive()));
        cd.setCriticalCases(parseNumber(cs.getCases().getCritical()));
        cd.setRecovered(parseNumber(cs.getCases().getRecovered()));
        cd.setCasesPerMillion(parseNumber(cs.getCases().getMillionPop()));
        cd.setTotalCases(parseNumber(cs.getCases().getTotal()));
        cd.setTestsPerMillion(parseNumber(cs.getTests().getMillionPop()));
        cd.setTotalTests(parseNumber(cs.getTests().getTotal()));
        cd.setNewDeaths(parseNumber(cs.getDeaths().getNewCases()));
        cd.setDeathsPerMillion(parseNumber(cs.getDeaths().getMillionPop()));
        cd.setTotalDeaths(parseNumber(cs.getDeaths().getTotal()));
        return cd;
    }

    /** Converts an array of CountryStatistic to a list of CovidDetails */
    public List<CovidDetails> convert(CountryStatistic[] stats){
        List<CovidDetails> list = new ArrayList<>();
        for(CountryStatistic cs: stats){
            list.add(convert(cs));
        }
        return list;
    }

    /** Reduces a list with several CovidDetails into a smaller one without duplicated days */
    public List<CovidDetails> reduce(List<CovidDetails> stats){
        List<CovidDetails> reducedList = new ArrayList<>();
        List<CovidDetails> itemsToRemove = new ArrayList<>();
        CovidDetails temp;

        while (stats.size() > 0){
            itemsToRemove.add(stats.get(0));
            // Check items with the same day
            for(int i = 1; i < stats.size(); i++){
                if(isSameDay(itemsToRemove.get(0).getDay(), stats.get(i).getDay()))
                    itemsToRemove.add(stats.get(i));
            }
            // Check the most recent item
            temp = itemsToRemove.get(0);
            for(int i = 1; i < itemsToRemove.size(); i++){
                if(temp.getTime().compareTo(itemsToRemove.get(i).getTime()) < 0)
                    temp = itemsToRemove.get(i);
            }
            // Add the most recent entry for a day
            reducedList.add(temp);
            // Remove items from the list
            stats.removeAll(itemsToRemove);
            // Clear other days
            itemsToRemove.clear();
        }
        return reducedList;
    }

    public int parseNumber(String number){
        if(number == null || number.contains("null"))
            return -1;
        if(number.startsWith("'") || number.endsWith("\""))
            number = number.substring(1, number.length() - 1);
        try{
            return Integer.parseInt(number);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // MOVE TO ANOTHER  CLASS
    // Same functionality as DateUtils
    // https://commons.apache.org/proper/commons-lang/javadocs/api-2.6/org/apache/commons/lang/time/DateUtils.html#isSameDay(java.util.Calendar,%20java.util.Calendar)

    public boolean isSameDay(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null)
            return false;
        return (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR));
    }


}
