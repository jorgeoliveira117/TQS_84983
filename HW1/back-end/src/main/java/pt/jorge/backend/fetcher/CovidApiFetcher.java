package pt.jorge.backend.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.entities.helper.CountryStatisticsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class CovidApiFetcher {

    // Not the best aproach, but as the git repository is only shared with the teacher it should be 'safe';
    private static final String API_KEY = "8b7adc7dd1mshc90568bcfe94194p19bf1ajsnb9339fef134f";

    private static final Logger log = LoggerFactory.getLogger(CovidApiFetcher.class);


    private final HttpEntity<CountryStatisticsResponse> request;
    private final RestTemplate restTemplate;

    @Autowired
    public CovidApiFetcher(RestTemplateBuilder builder){
        restTemplate = builder.build();
        // Configure the headers for every request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-RapidAPI-Host", "covid-193.p.rapidapi.com");
        headers.set("X-RapidAPI-Key", API_KEY);
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
        log.info("Creating GET to {}", url);
        ResponseEntity<CountryStatisticsResponse> stats = doHttpGet(url);
        if(stats.getBody() == null){
            log.info("[{}] - No content", stats.getStatusCodeValue());
            return new ArrayList<>();
        }
        log.info("[{}] - {} results", stats.getStatusCodeValue() , stats.getBody().getResponse().length);
        CountryStatistic[] response = stats.getBody().getResponse();
        if(response.length == 0)
            return new ArrayList<>();
        return CovidDetails.convert(response);
    }
}
