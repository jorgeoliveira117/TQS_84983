package pt.jorge.backend.entities.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Death {

    @JsonProperty("new")
    private String newCases;
    @JsonProperty("1M_pop")
    private String millionPop;
    private String total;

    /** Empty constructor to initialize the variable*/
    public Death(){
        // Empty constructor to initialize the variable
    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public String getMillionPop() {
        return millionPop;
    }

    public void setMillionPop(String millionPop) {
        this.millionPop = millionPop;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Death{" +
                "newCases=" + newCases +
                ", millionPop=" + millionPop +
                ", total=" + total +
                '}';
    }
}
