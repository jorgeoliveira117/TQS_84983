package pt.jorge.backend.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pt.jorge.backend.cache.Cache;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.util.Countries;
import pt.jorge.backend.util.Dates;

import java.text.SimpleDateFormat;
import java.util.*;

@EnableScheduling
@Service
public class CovidService {

    // Used in every request
    private static final String TODAY_URL = "https://covid-193.p.rapidapi.com/statistics";
    private static final String HISTORY_URL = "https://covid-193.p.rapidapi.com/history";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger log = LoggerFactory.getLogger(CovidService.class);


    private final List<String> countries;
    private final List<String> continents;
    private Cache<CovidDetails> dailyCases;
    private List<String> dailyTop;
    private static final int SORTED_MIN = 50;
    private static final int HISTORY_DAYS_MIN = 200;
    private final int CACHE_CHECK = 60 * 1000; // once per minute
    private Map<String,Cache<CovidDetails>> olderCases;
    Calendar today;

    private CovidApiFetcher fetcher;

    @Autowired
    public CovidService(CovidApiFetcher fetcher){
        this.fetcher = fetcher;

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
        checkContinents();

        today = Calendar.getInstance();
    }


    /** Returns all countries */
    public List<String> getCountries(){
        return countries;
    }

    /** Returns statistics for a country */
    public CovidDetails getToday(String country){
        today = Calendar.getInstance();
        // A cheat to dodge the delay that the API has to refresh cases for the new day
        if(today.get(Calendar.HOUR) <= 8 && dailyCases.get(Dates.countryAndDate("all", today)) == null)
            today.add(Calendar.HOUR, -today.get(Calendar.HOUR));
        // Check if value is present through key
        String key = Dates.countryAndDate(country, today);
        if(dailyCases.containsKey(key)){
            return dailyCases.get(key);
        }
        // If not, fetch all current cases
        getToday();
        return dailyCases.get(key);
    }

    /** Returns statistics from every country */
    public List<CovidDetails> getToday(){
        // check if there are elements in the cache
        if(dailyCases.size() > 0)
            return new ArrayList<>(dailyCases.values());
        // obtain new statistics for every country
        List<CovidDetails> stats = fetcher.getFromURL(TODAY_URL);
        // return an empty list if there are no elements
        if(stats.isEmpty())
            return new ArrayList<>();

        // add statistics to the cache
        addToCache(stats, dailyCases);
        return stats;
    }
    /** Returns statistics from every continent */
    public List<CovidDetails> getContinents(){
        if(dailyCases.size() == 0)
            getToday();
        today = Calendar.getInstance();
        if(today.get(Calendar.HOUR) <= 8 && dailyCases.get(Dates.countryAndDate("all", today)) == null)
            today.add(Calendar.HOUR, -today.get(Calendar.HOUR));
        List<CovidDetails> contDetails = new ArrayList<>();
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
        country = country.toLowerCase();
        Cache<CovidDetails> cache;
        if(olderCases.containsKey(country)){
            cache = olderCases.get(country);
        }else{
            log.debug("Created new cache for {}", country);
            cache = new Cache<>(1800 * 1000L, country);
        }
        List<CovidDetails> stats;
        // Check if there are suficient entries to give an evolution
        if(cache.size() < HISTORY_DAYS_MIN){
            String url = HISTORY_URL + "?country=" + country;
            stats = CovidDetails.reduce(fetcher.getFromURL(url));
            if(!stats.isEmpty())
                addToCache(stats, cache);
        }else{
            stats = new ArrayList<>(cache.values());
        }
        cache.resetAll();
        if(stats.isEmpty())
            return new ArrayList<>();
        olderCases.put(country, cache);
        return stats;
    }
    /** Returns every statistics from a given country on a given day*/
    public List<CovidDetails> getHistory(String country, Calendar day){
        // This function ignores the cache as several entries are required but only one of them is stored
        String url = HISTORY_URL + "?country=" + country + "&day=" + sdf.format(day.getTime());
        List<CovidDetails> stats = fetcher.getFromURL(url);
        if(stats.isEmpty())
            return new ArrayList<>();
        return stats;
    }
    /** Returns the most recent statistic from a given country on a given day*/
    public CovidDetails getHistorySingle(String country, Calendar day){
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
            log.debug("Created new cache for {}", country);
            cache = new Cache<>(1800 * 1000L, country);
        }
        // Get entry for that day
        List<CovidDetails> hist = CovidDetails.reduce(getHistory(country, day));
        if(hist.isEmpty())
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
        for(Cache<CovidDetails> cache: olderCases.values()){
            cacheStats.add(cache.getStats());
        }
        return cacheStats;
    }



    private void sortTopCases(){
        today = Calendar.getInstance();
        if(today.get(Calendar.HOUR) <= 8 && dailyCases.get(Dates.countryAndDate("all", today)) == null)
            today.add(Calendar.HOUR, -today.get(Calendar.HOUR));
        String todayString = sdf.format(today.getTime());

        log.debug("Checking for top cases...");
        // Check if dailyTop is from today
        if(!dailyTop.isEmpty() && dailyTop.size() > SORTED_MIN && dailyTop.get(0).contains(todayString)){
            log.debug("Top cases are already sorted");
            return;
        }
        else{
            // if dailyTop list is lower than the minimum required or not from today, get new cases
            addToCache(getToday(), dailyCases);
        }
        log.debug("Top cases need to be calculated");
        // get dailyCases that contains cases from today
        List<String> keysToCheck = new ArrayList<>();
        for(String key : dailyCases.keySet()){
            if(key.contains(todayString) && !dailyCases.get(key).getCountry().equalsIgnoreCase(dailyCases.get(key).getContinent()))
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
        log.debug("Checking available continents");
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
    @Scheduled(fixedDelay=CACHE_CHECK)
    private void clearExpired(){
        log.debug("Checking for expired cache keys...");
        List<String> keysToRemove = new ArrayList<>();
        dailyCases.clearExpired();
        for(Map.Entry<String, Cache<CovidDetails>> entry: olderCases.entrySet()){
            entry.getValue().clearExpired();
            if(entry.getValue().size() == 0)
                keysToRemove.add(entry.getKey());
        }
        for(String key: keysToRemove){
            log.info("Removed cache for " + key);
            olderCases.remove(key);
        }
    }
}
