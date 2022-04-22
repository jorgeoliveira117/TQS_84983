package pt.jorge.backend.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.entities.helper.CountryStatisticsResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service
public class CovidFetcher {

    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(CovidFetcher.class);
    // Not the best aproach, but as the git repository is only shared with the teacher it should be 'safe';
    private static final String apiKey = "8b7adc7dd1mshc90568bcfe94194p19bf1ajsnb9339fef134f";



    // Used in every request
    private final HttpEntity<CountryStatisticsResponse> request;

    private final String todayURL = "https://covid-193.p.rapidapi.com/statistics";
    private final String historyURL = "https://covid-193.p.rapidapi.com/history";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
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

    private ResponseEntity<CountryStatisticsResponse> doHttpGet(String url){
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                CountryStatisticsResponse.class
        );
    }

    public List<CovidDetails> getFromURL(String url){
        log.info("Creating GET to " + url);
        ResponseEntity<CountryStatisticsResponse> stats = doHttpGet(url);
        if(stats.getBody() == null){
            log.info("[" + stats.getStatusCodeValue() + "] - No content");
        }
        log.info("[" + stats.getStatusCodeValue() + "] - " + stats.getBody().getResponse().length + " results");
        CountryStatistic[] response = stats.getBody().getResponse();
        if(response.length == 0)
            return null;
        return CovidDetails.convert(response);
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
        return CovidDetails.reduce(stats);
    }
    /** Returns every statistics from a given country on a give day*/
    public List<CovidDetails> getHistory(String country, Calendar day){
        String url = historyURL + "?country=" + country + "&day=" + sdf.format(day.getTime());
        List<CovidDetails> stats = getFromURL(url);
        if(stats == null || stats.size() == 0)
            return null;
        return stats;
    }
}
