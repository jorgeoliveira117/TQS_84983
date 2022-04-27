package pt.jorge.backend.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.jorge.backend.entities.CacheStats;
import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.fetcher.CovidService;

import java.util.*;

public class Cache<T> {
    // Key is String with format "country-date"
    private Map<String, Long> times;
    private Map<String, T> values;

    /** Statistics */
    private int successfulHits;
    private int missedHits;

    private String name;

    /** Time to live in milliseconds */
    private long ttl;

    private static final Logger log = LoggerFactory.getLogger(Cache.class);


    public Cache(long ttl){
        this.ttl = ttl;
        times = new HashMap<>();
        values = new TreeMap<>();
        missedHits = 0;
        successfulHits = 0;
        name = "";
    }

    public Cache(long ttl, String name){
        this(ttl);
        this.name = name;
    }

    public Cache(){
        this(1000);
    }

    public void setTimeToLive(long ttl){
        this.ttl = ttl;
    }

    public long getTimeToLive(){
        return ttl;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public int getSuccessfulHits(){
        return successfulHits;
    }

    public int getMissedHits(){
        return missedHits;
    }


    /** Put a value in the cache adding the current time */
    public T put(String key, T value){
        times.put(key, System.currentTimeMillis());
        values.put(key, value);
        return value;
    }

    public T get(String key){
        if(!values.containsKey(key))
            missedHits++;
        else
            successfulHits++;
        return values.get(key);
    }

    public Collection<T> values(){
        return values.values();
    }

    public Set<String> keySet(){
        return values.keySet();
    }

    public T remove(String key){
        times.remove(key);
        return values.remove(key);
    }

    public Set<Map.Entry<String, T>> entrySet(){
        return values.entrySet();
    }

    /** Reset the time for a certain key, if it exists */
    public boolean reset(String key){
        if(values.containsKey(key)){
            times.put(key, System.currentTimeMillis());
            return true;
        }
        return false;
    }
    /** Reset the time for all keys*/
    public void resetAll(){
        for(String key: values.keySet())
            this.reset(key);
    }

    /** Clears expired keys. Returns number of removed keys*/
    public int clearExpired() {
        // To remove keys safely, only remove after checking all keys
        List<String> keysToClear = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            if ((currentTime - entry.getValue()) > ttl) {
                keysToClear.add(entry.getKey());
            }
        }
        for (String key : keysToClear) {
            log.debug("Removing key {} from cache", key);
            times.remove(key);
            values.remove(key);
        }
        return keysToClear.size();
    }

    public void clear(){
        times.clear();
        values.clear();
    }

    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    public boolean containsValue(CountryStatistic value) {
        return values.containsValue(value);
    }

    public int size() {
        return values.size();
    }

    public CacheStats getStats(){
        return new CacheStats(name, ttl, values.size(), successfulHits, missedHits);
    }

}
