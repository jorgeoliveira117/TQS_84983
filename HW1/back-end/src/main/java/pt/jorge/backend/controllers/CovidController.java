package pt.jorge.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.jorge.backend.cache.Cache;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.CovidDetailsSimple;
import pt.jorge.backend.fetcher.CovidFetcher;
import pt.jorge.backend.util.Countries;
import pt.jorge.backend.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CovidController {

    private static final Logger log = LoggerFactory.getLogger(CovidFetcher.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // Minimum amount of countries to not request a sort
    private static final int SORTED_MIN = 50;
    private Calendar today;

    // List with available countries and continents
    private final List<String> countries;
    private List<String> continents;
    // Cache where today's cases will be stored
    private Cache<CovidDetails> dailyCases;
    // List of dailyCases keys sorted by newCases
    private List<String> dailyTop;
    // Cache for older cases
    private Cache<CovidDetails> olderCases;
    // Cache for all world cases
    private Cache<CovidDetails> worldCases;
    // Covid API fetcher
    private CovidFetcher fetcher;

    public CovidController(RestTemplateBuilder builder){
        today = Calendar.getInstance();
        fetcher = new CovidFetcher(builder);
        continents = new ArrayList<>();
        dailyTop = new ArrayList<>();
        countries = Countries.getCountries();
        // Daily cache has a 30min ttl
        dailyCases = new Cache<>(1800 * 1000);
        // Older cache has a 15 min ttl
        olderCases = new Cache<>( 900 * 1000);
        // World cache has a 2h ttl
        worldCases = new Cache<>(2 * 3600 * 1000);

        // Fill dailyCases and worldCases
        addToCache(fetcher.getToday(), dailyCases);
        sortTopCases();
        //addToCache(fetcher.getHistory("all"), worldCases);
        checkContinents();
    }

    /** Returns all countries*/
    @GetMapping("/countries")
    private List<String> countries() {
        log.info("GET /countries");
        clearExpired();
        return countries;
    }

    /** Returns today's Covid Details for a country*/
    @GetMapping("/cases/{country}")
    private CovidDetails casesForCountryToday(@PathVariable String country) {
        log.info("GET /cases/" + country);
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR

        if(dailyCases.size() == 0)
            addToCache(fetcher.getToday(), dailyCases);
        CovidDetails detailsForCountry = dailyCases.get(StringConverter.countryAndDate(country, Calendar.getInstance()));

        clearExpired();
        return detailsForCountry;
    }

    /** Returns Covid Details for a country on a given day*/
    @GetMapping("/cases/{country}/{date}")
    private CovidDetails casesForCountryDate(@PathVariable String country, @PathVariable String date) {
        log.info("GET /cases/" + country + "/" + date);
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        Calendar day = Calendar.getInstance();
        try{
            day.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
            // ALTERAR
            // ALTERAR
            // ALTERAR
            // ALTERAR
        }
        String key = StringConverter.countryAndDate(country, day);
        CovidDetails detailsForCountry;
        if(!olderCases.containsKey(key))
            addToCache(fetcher.getHistory(country, day), olderCases);
        else
            olderCases.reset(key);
        detailsForCountry= olderCases.get(key);

        clearExpired();
        return detailsForCountry;
    }

    /** Returns today's cases for each continent*/
    @GetMapping("/cases/continents")
    private List<CovidDetails> casesForEachContinent() {
        log.info("GET /cases/continents");
        if(dailyCases.size() == 0)
            addToCache(fetcher.getToday(), dailyCases);

        today = Calendar.getInstance();
        List<CovidDetails> contDetails = new ArrayList<>();
        for(String continent: continents)
            contDetails.add(dailyCases.get(StringConverter.countryAndDate(continent, today)));

        clearExpired();
        return contDetails;
    }

    /** Returns today's cases for the whole world*/
    @GetMapping("/cases")
    private List<CovidDetails> casesWorld() {
        log.info("GET /cases/world");
        if(dailyCases.size() == 0)
            addToCache(fetcher.getToday(), dailyCases);

        clearExpired();
        return new ArrayList<CovidDetails>(dailyCases.values());
    }

    /** Returns Simple Covid Details for the top n countries with most cases*/
    @GetMapping("/cases/top/{n}")
    private List<CovidDetailsSimple> topCases(@PathVariable int n) {
        log.info("GET /cases/top/" + n);
        if(n < 0){
            clearExpired();
            return new ArrayList<>();
        }
        if(dailyCases.size() == 0)
            addToCache(fetcher.getToday(), dailyCases);

        sortTopCases();

        if(n >= dailyTop.size())
            n = dailyTop.size() - 1;

        List<CovidDetailsSimple> topCases = new ArrayList<>();
        for(int i = 0; i < n && i < dailyTop.size(); i++)
            topCases.add(dailyCases.get(dailyTop.get(i)));

        clearExpired();
        return topCases;
    }

    /** Returns the evolution of the last 7 days for a countries*/
    @GetMapping("/cases/evolution/{country}")
    private List<CovidDetails> evolution(@PathVariable String country) {
        log.info("GET /cases/evolution/" + country);
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        // CHECK IF COUNTRY IS VALID, THROW ERROR
        List<CovidDetails> evolution = new ArrayList<>();
        Calendar day = Calendar.getInstance();
        if(dailyCases.size() == 0)
            addToCache(fetcher.getToday(), dailyCases);
        String key = StringConverter.countryAndDate(country, day);
        evolution.add(dailyCases.get(key));
        for(int i = 1; i < 7; i++){
            day.add(Calendar.DAY_OF_MONTH, -1);
            key = StringConverter.countryAndDate(country, day);
            if(!olderCases.containsKey(key))
                addToCache(fetcher.getHistory(country, day), olderCases);
            else
                olderCases.reset(key);
            evolution.add(olderCases.get(key));
        }
        clearExpired();
        return evolution;
    }

    /** Returns the entire evolution of covid in the world*/
    @GetMapping("/cases/world/evolution")
    private List<CovidDetailsSimple> evolution() {
        log.info("GET /cases/world/evolution");

        if(worldCases.size() == 0)
            addToCache(fetcher.getHistory("all"), worldCases);

        clearExpired();
        return new ArrayList<>(worldCases.values());
    }

    private void sortTopCases(){
        today = Calendar.getInstance();
        String todayString = sdf.format(today.getTime());
        // Check if dailyTop is from today
        if(dailyTop != null){
            if(dailyTop.size() < SORTED_MIN)
                // if dailyTop list is lower than the minimum required, get new cases
                addToCache(fetcher.getToday(), dailyCases);
            else{
                // if an element from dailyTop is not from today, get new cases
                if(!dailyTop.get(0).contains(todayString))
                    addToCache(fetcher.getToday(), dailyCases);
            }
        }
        // get dailyCases that contains cases from today
        List<String> keysToCheck = new ArrayList<>();
        for(String key : dailyCases.keySet()){
            if(key.contains(todayString))
                keysToCheck.add(key);
        }
        // Only sort today's cases
        dailyTop = new ArrayList<>();
        int max;
        String maxKey;
        while(!keysToCheck.isEmpty()){
            max = -10;
            maxKey = "";
            for(String key: keysToCheck){
                if(dailyCases.get(key).getNewCases() > max){
                    max = dailyCases.get(key).getNewCases();
                    maxKey = key;
                }else{
                    if(dailyCases.get(key).getNewCases() == max
                            && !maxKey.isEmpty()
                            && dailyCases.get(key).getPopulation() < dailyCases.get(maxKey).getPopulation()){
                        max = dailyCases.get(key).getNewCases();
                        maxKey = key;
                    }
                }
            }
            dailyTop.add(maxKey);
            keysToCheck.remove(maxKey);
        }
    }

    /** Checks continents and adds them to a list */
    private void checkContinents(){
        for(CovidDetails cd : dailyCases.values()){
            if(cd.getCountry().equalsIgnoreCase(cd.getContinent()))
                continents.add(cd.getCountry());
        }
        Collections.sort(continents);
    }

    /** Adds several values to a cache */
    private void addToCache(List<CovidDetails> list, Cache<CovidDetails> cache){
        for(CovidDetails cd: list)
            cache.put(StringConverter.countryAndDate(cd.getCountry(), cd.getTime()), cd);
    }

    /** Adds one value to a cache */
    private void addToCache(CovidDetails cd, Cache<CovidDetails> cache){
        cache.put(StringConverter.countryAndDate(cd.getCountry(), cd.getTime()), cd);
    }

    // This function is called once per request and not periodically,
    //      to heavily reduce constraint and avoid conficts
    /** Clears expired cache keys */
    private void clearExpired(){
        dailyCases.clearExpired();
        olderCases.clearExpired();
        worldCases.clearExpired();
    }
}
