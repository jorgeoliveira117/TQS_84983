package pt.jorge.backend.entities.helper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryStatistic {

    private String continent;
    private String country;
    private int population;
    private Case cases;
    private Death deaths;
    private Test tests;
    private Calendar day;
    private Calendar time;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    /** Empty constructor to initialize the variable*/
    public CountryStatistic() {
        // Empty constructor to initialize the variable
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Case getCases() {
        return cases;
    }

    public void setCases(Case cases) {
        this.cases = cases;
    }

    public Death getDeaths() {
        return deaths;
    }

    public void setDeaths(Death deaths) {
        this.deaths = deaths;
    }

    public Test getTests() {
        return tests;
    }

    public void setTests(Test tests) {
        this.tests = tests;
    }

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CountryStatistic{" +
                "continent=" + continent +
                ", country=" + country +
                ", population=" + population +
                ", cases=" + cases +
                ", deaths=" + deaths +
                ", tests=" + tests +
                ", day=" + sdf.format(day.getTime()) +
                ", time=" + sdfTime.format(time.getTime()) +
                '}';
    }
}

