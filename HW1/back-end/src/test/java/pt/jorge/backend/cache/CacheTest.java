package pt.jorge.backend.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.jorge.backend.entities.helper.CountryStatistic;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache cache;
    private CountryStatistic csPortugal;
    private CountryStatistic csSpain;
    private final String portugal = "portugal";
    private final String spain = "spain";

    @BeforeEach
    void setUp() {
        cache = new Cache(100*1000);
        // Create a Statistics objects. They can be empty as their content doesn't matter
        csPortugal = new CountryStatistic();
        csSpain = new CountryStatistic();


    }

    @Test
    @DisplayName("Two values should be available after putting them in the cache")
    void putTwoValues(){
        cache.put(portugal, csPortugal);
        cache.put(spain, csSpain);
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey(portugal));
        assertTrue(cache.containsKey(spain));
        assertEquals(csPortugal, cache.get(portugal));
    }

    @Test
    @DisplayName("A value should still exist before expiring")
    void valueIsPresentAfterNSeconds() throws InterruptedException {
        cache.put(portugal, csPortugal);
        int seconds = 1000;
        cache.setTimeToLive(seconds*1000);
        Thread.sleep(500);
        cache.clearExpired();
        assertTrue(cache.containsKey(portugal));
        assertTrue(cache.containsValue(csPortugal));
    }

    @Test
    @DisplayName("A value should not exist after expiring")
    void valueIsNotPresentAfterNSeconds() throws InterruptedException {
        cache.put(portugal, csPortugal);
        int seconds = 1;
        cache.setTimeToLive(seconds*1000);
        Thread.sleep(2000);
        cache.clearExpired();
        assertFalse(cache.containsKey(portugal));
        assertFalse(cache.containsValue(csPortugal));
    }

    @Test
    @DisplayName("A value should not exist after expiring but a recent one should")
    void oneValueIsNotPresent() throws InterruptedException {
        cache.put(portugal, csPortugal);
        int seconds = 1;
        cache.setTimeToLive(seconds*1000);
        Thread.sleep(2000);
        cache.put(spain, csSpain);
        cache.clearExpired();
        assertFalse(cache.containsKey(portugal));
        assertTrue(cache.containsKey(spain));
    }

    @Test
    @DisplayName("A value should still exist after reseting its' time")
    void valuePresentAfterReset() throws InterruptedException {
        cache.put(portugal, csPortugal);
        int seconds = 1;
        cache.setTimeToLive(seconds*1000);
        // wait 0.5 seconds and reset time
        Thread.sleep(500);
        cache.reset(portugal);
        // check if value still exists
        cache.clearExpired();
        assertTrue(cache.containsKey(portugal));
        // wait 0.6 seconds and check if value still exists
        Thread.sleep(600);
        cache.clearExpired();
        assertTrue(cache.containsKey(portugal));
        // wait 0.6 seconds and check that value no longer exists
        Thread.sleep(600);
        cache.clearExpired();
        assertFalse(cache.containsKey(portugal));
    }


}