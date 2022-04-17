package pt.jorge.backend;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pt.jorge.backend.entities.CountryStatistic;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryStatisticList {
    private List<CountryStatistic> statsList;

    public CountryStatisticList(){
        statsList = new ArrayList<>();
    }

    public List<CountryStatistic> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<CountryStatistic> statsList) {
        this.statsList = statsList;
    }

    @Override
    public String toString() {
        StringBuilder stats = new StringBuilder();
        for(CountryStatistic cs : statsList){
            stats.append(cs.toString()).append(", ");
        }
        return "CountryStatisticList{" +
                "statsList=[" +
                stats.toString() +
                "]}";
    }
}
