package pt.jorge.backend.cache;

import pt.jorge.backend.entities.helper.CountryStatistic;

import java.util.*;

public class Cache<T> {
    // Key is String with format "country-date"
    private Map<String, Long> times;
    private Map<String, T> values;

    /** Time to live in milliseconds */
    private long ttl;

    public Cache(long ttl){
        this.ttl = ttl;
        times = new HashMap<>();
        values = new HashMap<>();
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

    /** Put a value in the cache adding the current time */
    public T put(String key, T value){
        times.put(key, System.currentTimeMillis());
        values.put(key, value);
        return value;
    }

    public T get(String key){
        return values.get(key);
    }

    public Collection<T> values(){
        return values.values();
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

}
