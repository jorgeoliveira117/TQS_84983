package pt.jorge.backend.entities;


import pt.jorge.backend.entities.helper.Case;
import pt.jorge.backend.entities.helper.Death;
import pt.jorge.backend.entities.helper.Test;

import java.util.Calendar;

public class CovidDetailsSimple {
    /** Simple Covid Details for a certain country*/

    // Basic Country info
    private String continent;
    private String country;
    private int population;
    // Day of statistic
    private Calendar day;
    // Stats related to cases
    private int newCases;
    private int activeCases;
    // Stats related to deaths
    private int newDeaths;


    public CovidDetailsSimple(){}

    public CovidDetailsSimple(String continent, String country, int population, Calendar day, int newCases, int activeCases, int newDeaths) {
        this.continent = continent;
        this.country = country;
        this.population = population;
        this.day = day;
        this.newCases = newCases;
        this.activeCases = activeCases;
        this.newDeaths = newDeaths;
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

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

    public int getNewCases() {
        return newCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public int getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(int activeCases) {
        this.activeCases = activeCases;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    @Override
    public String toString() {
        return "CovidDetailsSimple{" +
                "continent='" + continent + '\'' +
                ", country='" + country + '\'' +
                ", population=" + population +
                ", day=" + day +
                ", newCases=" + newCases +
                ", activeCases=" + activeCases +
                ", newDeaths=" + newDeaths +
                '}';
    }
}
