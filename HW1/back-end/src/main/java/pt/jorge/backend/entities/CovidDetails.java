package pt.jorge.backend.entities;


import pt.jorge.backend.entities.helper.CountryStatistic;
import pt.jorge.backend.util.Dates;

import java.util.ArrayList;
import java.util.List;

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

    // Static Functions

    /** Converts a CountryStatistic object to CovidDetails object */
    public static CovidDetails convert(CountryStatistic cs){
        CovidDetails cd = new CovidDetails();
        cd.setCountry(cs.getCountry());
        cd.setContinent(cs.getContinent());
        cd.setDay(cs.getDay());
        cd.setTime(cs.getTime());
        cd.setPopulation(cs.getPopulation());
        cd.setNewCases(parseNumber(cs.getCases().getNewCases()));
        cd.setActiveCases(parseNumber(cs.getCases().getActive()));
        cd.setCriticalCases(parseNumber(cs.getCases().getCritical()));
        cd.setRecovered(parseNumber(cs.getCases().getRecovered()));
        cd.setCasesPerMillion(parseNumber(cs.getCases().getMillionPop()));
        cd.setTotalCases(parseNumber(cs.getCases().getTotal()));
        cd.setTestsPerMillion(parseNumber(cs.getTests().getMillionPop()));
        cd.setTotalTests(parseNumber(cs.getTests().getTotal()));
        cd.setNewDeaths(parseNumber(cs.getDeaths().getNewCases()));
        cd.setDeathsPerMillion(parseNumber(cs.getDeaths().getMillionPop()));
        cd.setTotalDeaths(parseNumber(cs.getDeaths().getTotal()));
        return cd;
    }

    /** Converts an array of CountryStatistic to a list of CovidDetails */
    public static List<CovidDetails> convert(CountryStatistic[] stats){
        List<CovidDetails> list = new ArrayList<>();
        for(CountryStatistic cs: stats){
            list.add(convert(cs));
        }
        return list;
    }

    /** Reduces a list with several CovidDetails into a smaller one without duplicated days */
    public static List<CovidDetails> reduce(List<CovidDetails> stats){
        if(stats == null)
            return new ArrayList<>();

        List<CovidDetails> reducedList = new ArrayList<>();
        List<CovidDetails> itemsToRemove = new ArrayList<>();
        CovidDetails temp;

        while (!stats.isEmpty()){
            itemsToRemove.add(stats.get(0));
            // Check items with the same day
            for(int i = 1; i < stats.size(); i++){
                if(Dates.isSameDay(itemsToRemove.get(0).getDay(), stats.get(i).getDay()))
                    itemsToRemove.add(stats.get(i));
            }
            // Check the most recent item
            temp = itemsToRemove.get(0);
            for(int i = 1; i < itemsToRemove.size(); i++){
                if(temp.getTime().compareTo(itemsToRemove.get(i).getTime()) < 0)
                    temp = itemsToRemove.get(i);
            }
            // Add the most recent entry for a day
            reducedList.add(temp);
            // Remove items from the list
            stats.removeAll(itemsToRemove);
            // Clear other days
            itemsToRemove.clear();
        }
        return reducedList;
    }

    public static int parseNumber(String number){
        if(number == null || number.contains("null"))
            return -1;
        if(number.startsWith("'") || number.startsWith("\""))
            number = number.substring(1, number.length() - 1);
        try{
            return Integer.parseInt(number);
        }catch (NumberFormatException e){
            return -1;
        }
    }
}
