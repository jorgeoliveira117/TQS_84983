package pt.jorge.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pt.jorge.backend.cache.Cache;
import pt.jorge.backend.entities.CovidDetails;
import pt.jorge.backend.entities.CovidDetailsSimple;
import pt.jorge.backend.util.Countries;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CovidController {

    private List<String> countries;
    // Cache where today's cases will be stored
    private Cache<CovidDetails> dailyCases;
    // Cache for older cases
    private Cache<CovidDetails> olderCases;
    // Cache for all world cases
    private Cache<CovidDetails> worldCases;
    // Cache for each continent's cases
    private Cache<CovidDetails> continentCases;

    public CovidController(){
        countries = Countries.getCountries();
        // Daily cache has a 24h ttl
        dailyCases = new Cache<>(24 * 3600 * 1000);
        // Older cache has a 2h ttl
        olderCases = new Cache<>(2 * 3600 * 1000);
        // World cache has a 24h ttl
        worldCases = new Cache<>(24 * 3600 * 1000);
        // Continents cache has a 24h ttl
        continentCases = new Cache<>(24 * 3600 * 1000);

        // Maybe fill dailyCases, worldCases and continentCases(?)
    }

    /** Returns all countries*/
    @GetMapping("/countries")
    private List<String> countries() {
        return countries;
    }

    /** Returns today's Covid Details for a country*/
    @GetMapping("/cases/{country}/")
    private CovidDetails casesForCountryToday(@PathVariable String country) {

        return new CovidDetails();
    }

    /** Returns Covid Details for a country on a given day*/
    @GetMapping("/cases/{country}/{date}")
    private CovidDetails casesForCountryDate(@PathVariable String country, @PathVariable String date) {

        return new CovidDetails();
    }

    /** Returns today's cases for each continent*/
    @GetMapping("/cases/continents")
    private List<CovidDetails> casesForEachContinent() {

        // Implement cache for this?
        return new ArrayList<>();
    }

    /** Returns today's cases for the whole world*/
    @GetMapping("/cases/world")
    private CovidDetails casesWorld() {

        // Implement cache for this?
        return new CovidDetails();
    }

    /** Returns Simple Covid Details for the top n countries with most cases*/
    @GetMapping("/cases/top/{n}")
    private List<CovidDetailsSimple> topCases(@PathVariable int n) {
        List<CovidDetailsSimple> top = new ArrayList<>();
        for(int i = 0; i < n; i++){
            top.add(new CovidDetails());
        }
        return top;
    }

    /** Returns the evolution of the last 7 days for a countries*/
    @GetMapping("/cases/evolution/{country}")
    private List<CovidDetails> evolution(@PathVariable String country) {
        List<CovidDetails> top = new ArrayList<>();
        top.add(new CovidDetails());
        return top;
    }

    /** Returns the entire evolution of covid in the world*/
    @GetMapping("/cases/world/evolution")
    private List<CovidDetailsSimple> evolution() {
        List<CovidDetailsSimple> top = new ArrayList<>();
        top.add(new CovidDetailsSimple());
        return top;
    }
}
