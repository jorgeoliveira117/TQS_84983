package pt.jorge.backend.entities.helper;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Case {

    @JsonProperty("new")
    private String newCases;
    private String active;
    private String critical;
    private String recovered;
    @JsonProperty("1M_pop")
    private String millionPop;
    private String total;

    /** Empty constructor to initialize the variable*/
    public Case(){
        // Empty constructor to initialize the variable
    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
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
        return "Case{" +
                "new=" + newCases +
                ", active=" + active +
                ", critical=" + critical +
                ", recovered=" + recovered +
                ", millionPop=" + millionPop +
                ", total=" + total +
                '}';
    }
}
