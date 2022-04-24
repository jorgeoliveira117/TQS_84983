package pt.jorge.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.jorge.backend.cache.Cache;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.CovidDetailsSimple;
import pt.jorge.backend.exceptions.CountryAndDateNotFoundException;
import pt.jorge.backend.exceptions.CountryNotFoundException;
import pt.jorge.backend.exceptions.InvalidDateException;
import pt.jorge.backend.exceptions.InvalidIdException;
import pt.jorge.backend.fetcher.CovidFetcher;
import pt.jorge.backend.util.Countries;
import pt.jorge.backend.util.Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CovidController {

    private static final Logger log = LoggerFactory.getLogger(CovidFetcher.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // Minimum amount of countries to not request a sort
    private Calendar today;

    // Covid API fetcher
    private final CovidFetcher fetcher;

    public CovidController(CovidFetcher fetcher){
        today = Calendar.getInstance();
        this.fetcher = fetcher;
    }

    /** Returns all countries*/
    @GetMapping("/countries")
    private List<String> countries() {
        log.info("GET /countries");
        return fetcher.getCountries();
    }

    /** Returns today's Covid Details for a country*/
    @GetMapping("/cases/{country}")
    private CovidDetails casesForCountryToday(@PathVariable String country) {
        log.info("GET /cases/" + country);
        CovidDetails detailsForCountry = fetcher.getToday(country);
        if(detailsForCountry == null)
            throw new CountryNotFoundException(country);
        return detailsForCountry;
    }

    /** Returns Covid Details for a country on a given day*/
    @GetMapping("/cases/{country}/{date}")
    private CovidDetails casesForCountryDate(@PathVariable String country, @PathVariable String date) {
        log.info("GET /cases/" + country + "/" + date);

        Calendar day = Calendar.getInstance();
        try{
            day.setTime(sdf.parse(date));
        } catch (ParseException e) {
            throw new InvalidDateException(date);
        }

        CovidDetails detailsForCountry = fetcher.getHistorySingle(country, day);

        if(detailsForCountry == null)
            throw new CountryAndDateNotFoundException(country, date);
        return detailsForCountry;
    }

    /** Returns today's cases for each continent*/
    @GetMapping("/cases/continents")
    private List<CovidDetails> casesForEachContinent() {
        log.info("GET /cases/continents");

        return fetcher.getContinents();
    }

    /** Returns today's cases for the whole world*/
    @GetMapping("/cases")
    private List<CovidDetails> casesWorld() {
        log.info("GET /cases");
        return fetcher.getToday();
    }

    /** Returns Simple Covid Details for the top n countries with most cases*/
    @GetMapping("/cases/top/{n}")
    private List<CovidDetails> topCases(@PathVariable int n) {
        log.info("GET /cases/top/" + n);

        if(n < 0){
            throw new InvalidIdException(n);
        }

        return fetcher.getTop(n);
    }

    /** Returns the evolution of the last 7 days for a countries*/
    @GetMapping("/cases/evolution/{country}")
    private List<CovidDetails> evolution(@PathVariable String country) {
        log.info("GET /cases/evolution/" + country);

        CovidDetails cd = fetcher.getToday(country);
        if(cd == null)
            throw new CountryNotFoundException(country);

        Calendar day = Calendar.getInstance();
        List<CovidDetails> evolution = new ArrayList<>();
        evolution.add(cd);

        for(int i = 1; i < 7; i++){
            day.add(Calendar.DAY_OF_MONTH, -1);
            cd = fetcher.getHistorySingle(country, day);
            if(cd != null)
                evolution.add(cd);
        }
        return evolution;
    }

    /** Returns the entire evolution of covid in the world*/
    @GetMapping("/cases/world/evolution")
    private List<CovidDetails> evolution() {
        log.info("GET /cases/world/evolution");

        return fetcher.getHistory("all");
    }

    // Maybe add an endpoint for the entire evolution of a certain country

    /** Returns statistics for every cache*/
    @GetMapping("/cache")
    private List<CacheStats> cacheStats() {
        log.info("GET /cache");

        return fetcher.getCacheStats();
    }
}
