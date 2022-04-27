package pt.jorge.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.exceptions.CountryAndDateNotFoundException;
import pt.jorge.backend.exceptions.CountryNotFoundException;
import pt.jorge.backend.exceptions.InvalidDateException;
import pt.jorge.backend.exceptions.InvalidIdException;
import pt.jorge.backend.fetcher.CovidService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
// this is required to allow requests from react in the same machine
@CrossOrigin(origins = "http://localhost:3000")
public class CovidController {

    private static final Logger log = LoggerFactory.getLogger(CovidController.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Covid API fetcher
    private final CovidService service;

    public CovidController(CovidService fetcher){
        this.service = fetcher;
    }

    /** Returns all countries*/
    @GetMapping("/countries")
    public List<String> countries() {
        log.info("GET /countries");
        List<String> countries = service.getCountries();
        log.info("Returning countries [{} entries]", countries.size());
        return countries;
    }

    /** Returns today's Covid Details for a country*/
    @GetMapping("/cases/{country}")
    public CovidDetails casesForCountryToday(@PathVariable String country) {
        log.info("GET /cases/{}", country);
        CovidDetails detailsForCountry = service.getToday(country);
        if(detailsForCountry == null){
            log.warn("Country {} not found", country);
            throw new CountryNotFoundException(country);
        }
        log.info("Returning today's cases for {}", country);
        return detailsForCountry;
    }

    /** Returns Covid Details for a country on a given day*/
    @GetMapping("/cases/{country}/{date}")
    public CovidDetails casesForCountryDate(@PathVariable String country, @PathVariable String date) {
        log.info("GET /cases/{}/{}", country, date);

        Calendar day = Calendar.getInstance();
        try{
            day.setTime(sdf.parse(date));
        } catch (ParseException e) {
            log.warn("Invalid date {}", date);
            throw new InvalidDateException(date);
        }

        CovidDetails detailsForCountry = service.getHistorySingle(country, day);

        if(detailsForCountry == null){
            log.warn("Cases not found for {} on {}", country, date);
            throw new CountryAndDateNotFoundException(country, date);
        }

        log.info("Returning cases for {} on {}", country, date);
        return detailsForCountry;
    }

    /** Returns today's cases for each continent*/
    @GetMapping("/cases/continents")
    public List<CovidDetails> casesForEachContinent() {
        log.info("GET /cases/continents");
        List<CovidDetails> continentCases  = service.getContinents();
        log.info("Returning today's cases for continents [{} entries]", continentCases.size());
        return continentCases;
    }

    /** Returns today's cases for the whole world*/
    @GetMapping("/cases")
    public List<CovidDetails> casesWorld() {
        log.info("GET /cases");
        List<CovidDetails> todayCases  = service.getToday();
        log.info("Returning today's cases [{} entries]", todayCases.size());
        return todayCases;
    }

    /** Returns Simple Covid Details for the top n countries with most cases*/
    @GetMapping("/cases/top/{n}")
    public List<CovidDetails> topCases(@PathVariable int n) {
        log.info("GET /cases/top/{}", n);

        if(n < 0){
            log.warn("Id {} is invalid", n);
            throw new InvalidIdException(n);
        }
        List<CovidDetails> topCases  = service.getTop(n);
        log.info("Returning the {} top cases", topCases.size());
        return topCases;
    }

    /** Returns the evolution of the last 7 days for a countries*/
    @GetMapping("/cases/evolution/{country}")
    public List<CovidDetails> evolution(@PathVariable String country) {
        log.info("GET /cases/evolution/{}", country);

        CovidDetails cd = service.getToday(country);
        if(cd == null){
            log.warn("Country {} not found", country);
            throw new CountryNotFoundException(country);
        }

        Calendar day = Calendar.getInstance();
        List<CovidDetails> evolution = new ArrayList<>();
        evolution.add(cd);

        for(int i = 1; i < 7; i++){
            day.add(Calendar.DAY_OF_MONTH, -1);
            cd = service.getHistorySingle(country, day);
            if(cd != null)
                evolution.add(cd);
        }
        log.info("Returning Last 7 days for {}", country);
        return evolution;
    }

    /** Returns the entire evolution of covid in the world*/
    @GetMapping("/cases/world/evolution")
    public List<CovidDetails> evolution() {
        log.info("GET /cases/world/evolution");
        List<CovidDetails> evolution =  service.getHistory("all");
        log.info("Returning World evolution with {} entries", evolution.size());
        return evolution;
    }

    // Maybe add an endpoint for the entire evolution of a certain country

    /** Returns statistics for every cache*/
    @GetMapping("/cache")
    public List<CacheStats> cacheStats() {
        log.info("GET /cache");
        List<CacheStats> cacheStats =  service.getCacheStats();
        log.info("Returning {} cache statistics", cacheStats.size());
        return cacheStats;
    }
}
