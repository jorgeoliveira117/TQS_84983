package pt.jorge.backend.entities;


import pt.jorge.backend.entities.helper.Case;
import pt.jorge.backend.entities.helper.Death;
import pt.jorge.backend.entities.helper.Test;

import javax.persistence.Entity;
import java.util.Calendar;

public class CovidDetails extends CovidDetailsSimple{
    /** Full Covid Details for a certain country*/

    // Stats related to cases
    private int criticalCases;
    private int recovered;
    private int casesPerMillion;
    private int totalCases;
    // Stats related to deaths
    private int deathsPerMillion;
    private int totalDeaths;
    // Stats related to tests
    private int testsPerMillion;
    private int totalTests;

    public CovidDetails(){
        super();
    }

    public CovidDetails(String continent, String country, int population, Calendar day, int newCases, int activeCases, int newDeaths, int criticalCases, int recovered, int casesPerMillion, int totalCases, int deathsPerMillion, int totalDeaths, int testsPerMillion, int totalTests) {
        super(continent, country, population, day, newCases, activeCases, newDeaths);
        this.criticalCases = criticalCases;
        this.recovered = recovered;
        this.casesPerMillion = casesPerMillion;
        this.totalCases = totalCases;
        this.deathsPerMillion = deathsPerMillion;
        this.totalDeaths = totalDeaths;
        this.testsPerMillion = testsPerMillion;
        this.totalTests = totalTests;
    }

    public int getCriticalCases() {
        return criticalCases;
    }

    public void setCriticalCases(int criticalCases) {
        this.criticalCases = criticalCases;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getCasesPerMillion() {
        return casesPerMillion;
    }

    public void setCasesPerMillion(int casesPerMillion) {
        this.casesPerMillion = casesPerMillion;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getDeathsPerMillion() {
        return deathsPerMillion;
    }

    public void setDeathsPerMillion(int deathsPerMillion) {
        this.deathsPerMillion = deathsPerMillion;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTestsPerMillion() {
        return testsPerMillion;
    }

    public void setTestsPerMillion(int testsPerMillion) {
        this.testsPerMillion = testsPerMillion;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    @Override
    public String toString() {
        return "CovidDetails{" +
                super.toString() +
                "criticalCases=" + criticalCases +
                ", recovered=" + recovered +
                ", casesPerMillion=" + casesPerMillion +
                ", totalCases=" + totalCases +
                ", deathsPerMillion=" + deathsPerMillion +
                ", totalDeaths=" + totalDeaths +
                ", testsPerMillion=" + testsPerMillion +
                ", totalTests=" + totalTests +
                '}';
    }
}
