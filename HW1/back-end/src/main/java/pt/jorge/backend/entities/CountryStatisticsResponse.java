package pt.jorge.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryStatisticsResponse {
    private String get;
    private CountryStatistic[] response;


    public CountryStatisticsResponse(){

    }

    public CountryStatistic[] getResponse() {
        return response;
    }

    public void setResponse(CountryStatistic[] response) {
        this.response = response;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    @Override
    public String toString() {
        StringBuilder stats = new StringBuilder();
        for(CountryStatistic cs : response){
            stats.append(cs.toString()).append(", ");
        }
        return "CountryStatisticsResponse{" +
                "response=" + stats +
                ", get='" + get + '\'' +
                '}';
    }
}
