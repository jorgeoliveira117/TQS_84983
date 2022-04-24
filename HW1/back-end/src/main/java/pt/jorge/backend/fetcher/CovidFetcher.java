package pt.jorge.backend.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pt.jorge.backend.cache.Cache;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.CovidDetailsSimple;
import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.entities.helper.CountryStatisticsResponse;
import pt.jorge.backend.util.Countries;
import pt.jorge.backend.util.Dates;

import java.text.SimpleDateFormat;
import java.util.*;

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

    // List with available countries and continents
    private final List<String> countries;
    private final List<String> continents;
    // Cache where today's cases will be stored
    private Cache<CovidDetails> dailyCases;
    // List of dailyCases keys sorted by newCases
    private List<String> dailyTop;
    // A minimum threshold to require a new request of statistics
    private static final int SORTED_MIN = 50;
    private static final int HISTORY_DAYS_MIN = 200;
    // Map of caches for older cases
    private Map<String,Cache<CovidDetails>> olderCases;
    Calendar today;

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

        // Simple cache configuration
        continents = new ArrayList<>();
        dailyTop = new ArrayList<>();
        countries = Countries.getCountries();
        // Daily cache has a 30min ttl
        dailyCases = new Cache<>(1800 * 1000L, "Daily Cases");
        // Older cache has a 30 min ttl
        olderCases = new HashMap<>();

        // Fill dailyCases and worldCases
        addToCache(getToday(), dailyCases);
        sortTopCases();
        //addToCache(fetcher.getHistory("all"), worldCases);
        checkContinents();

        today = Calendar.getInstance();
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
            return new ArrayList<>();
        }
        log.info("[" + stats.getStatusCodeValue() + "] - " + stats.getBody().getResponse().length + " results");
        CountryStatistic[] response = stats.getBody().getResponse();
        if(response.length == 0)
            return new ArrayList<>();
        return CovidDetails.convert(response);
    }


    /** Returns all countries */
    public List<String> getCountries(){
        clearExpired();
        return countries;
    }

    /** Returns statistics for a country */
    public CovidDetails getToday(String country){
        clearExpired();

        today = Calendar.getInstance();
        // Check if value is present through key
        String key = Dates.countryAndDate(country, today);
        if(dailyCases.containsKey(key)){
            return dailyCases.get(key);
        }
        // If not, fetch all current cases
        getToday();
        return dailyCases.get(key);
    }
    /*
    // Old implementation, called for every country, now it just gets from all countries in cache
    public CovidDetails getToday(String country){
        String url = todayURL + "?country=" + country;
        List<CovidDetails> stats = getFromURL(url);
        if(stats == null || stats.size() == 0)
            return null;
        return stats.get(0);
    }
     */
    /** Returns statistics from every country */
    public List<CovidDetails> getToday(){
        clearExpired();
        // check if there are elements in the cache
        if(dailyCases.size() > 0)
            return new ArrayList<CovidDetails>(dailyCases.values());
        // obtain new statistics for every country
        List<CovidDetails> stats = getFromURL(todayURL);
        // return the list or null if there are no elements
        if(stats.size() == 0)
            return null;
        // add statistics to the cache
        addToCache(stats, dailyCases);
        return stats;
    }
    /** Returns statistics from every continent */
    public List<CovidDetails> getContinents(){
        clearExpired();

        if(dailyCases.size() == 0)
            getToday();

        today = Calendar.getInstance();
        List<CovidDetails> contDetails = new ArrayList<>();
        CovidDetails temp;
        String key;
        for(String continent: continents){
            key = Dates.countryAndDate(continent, today);
            if(dailyCases.containsKey(key))
                contDetails.add(dailyCases.get(key));
        }

        return contDetails;
    }
    /** Returns n countries with the most cases */
    public List<CovidDetails> getTop(int n){
        clearExpired();

        if(dailyCases.size() == 0)
            getToday();

        sortTopCases();

        if(n >= dailyTop.size())
            n = dailyTop.size() - 1;

        List<CovidDetails> topCases = new ArrayList<>();
        for(int i = 0; i < n && i < dailyTop.size(); i++)
            topCases.add(dailyCases.get(dailyTop.get(i)));

        return topCases;
    }
    /** Returns every daily statistic from a given country */
    public List<CovidDetails> getHistory(String country){
        clearExpired();

        country = country.toLowerCase();
        Cache<CovidDetails> cache;
        if(olderCases.containsKey(country)){
            cache = olderCases.get(country);
        }else{
            cache = new Cache<CovidDetails>(1800 * 1000L, country);
        }
        List<CovidDetails> stats;
        // Check if there are suficient entries to give an evolution
        if(cache.size() < HISTORY_DAYS_MIN){
            String url = historyURL + "?country=" + country;
            stats = CovidDetails.reduce(getFromURL(url));
            if(stats.size() > 0)
                addToCache(stats, cache);
        }else{
            stats = new ArrayList<>(cache.values());
        }
        cache.resetAll();
        if(stats.size() == 0)
            return null;
        olderCases.put(country, cache);
        return stats;
    }
    /** Returns every statistics from a given country on a given day*/
    public List<CovidDetails> getHistory(String country, Calendar day){
        clearExpired();
        // This function ignores the cache as several entries are required but only one of them is stored
        String url = historyURL + "?country=" + country + "&day=" + sdf.format(day.getTime());
        List<CovidDetails> stats = getFromURL(url);
        if(stats.size() == 0)
            return null;
        return stats;
    }
    /** Returns the most recent statistic from a given country on a given day*/
    public CovidDetails getHistorySingle(String country, Calendar day){
        clearExpired();
        country = country.toLowerCase();
        // Check if value is present through key
        String key = Dates.countryAndDate(country, day);
        Cache<CovidDetails> cache;
        if(olderCases.containsKey(country)){
            cache = olderCases.get(country);
            if(cache.containsKey(key)){
                cache.reset(key);
                return cache.get(key);
            }
        }else{
            cache = new Cache<CovidDetails>(1800 * 1000L, country);
        }
        // Get entry for that day
        List<CovidDetails> hist = CovidDetails.reduce(getHistory(country, day));
        if(hist == null || hist.size() == 0)
            return null;
        CovidDetails stat = hist.get(0);
        addToCache(stat, cache);
        olderCases.put(country, cache);
        return stat;
    }
    /** Returns statistics of every cache */
    public List<CacheStats> getCacheStats(){
        List<CacheStats> cacheStats = new ArrayList<>();
        cacheStats.add(dailyCases.getStats());
        for(String key: olderCases.keySet()){
            cacheStats.add(olderCases.get(key).getStats());
        }
        return cacheStats;
    }



    private void sortTopCases(){
        today = Calendar.getInstance();
        String todayString = sdf.format(today.getTime());
        // Check if dailyTop is from today
        if(dailyTop != null){
            if(dailyTop.size() < SORTED_MIN)
                // if dailyTop list is lower than the minimum required, get new cases
                addToCache(getToday(), dailyCases);
            else{
                // if an element from dailyTop is not from today, get new cases
                if(!dailyTop.get(0).contains(todayString))
                    addToCache(getToday(), dailyCases);
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
        if(list == null)
            return;
        for(CovidDetails cd: list)
            addToCache(cd, cache);
    }

    /** Adds one value to a cache */
    private void addToCache(CovidDetails cd, Cache<CovidDetails> cache){
        if(cd == null)
            return;
        cache.put(Dates.countryAndDate(cd.getCountry(), cd.getDay()), cd);
    }

    // This function is called once per request and not periodically,
    //      to heavily reduce constraint and avoid conficts
    /** Clears expired cache keys */
    private void clearExpired(){
        dailyCases.clearExpired();
        for(String country: olderCases.keySet()){
            olderCases.get(country).clearExpired();
            // Remove cache if it has no content
            if(olderCases.get(country).size() == 0)
                olderCases.remove(country);
        }
    }


}
