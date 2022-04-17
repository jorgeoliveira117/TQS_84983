package pt.jorge.backend.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Case {

    private String newCases;
    private int active;
    private int critical;
    private int recovered;
    private String millionPop;
    private int total;

    public Case(){

    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public String getMillionPop() {
        return millionPop;
    }

    public void setMillionPop(String millionPop) {
        this.millionPop = millionPop;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Case{" +
                "newCases='" + newCases + '\'' +
                ", active=" + active +
                ", critical=" + critical +
                ", recovered=" + recovered +
                ", millionPop='" + millionPop + '\'' +
                ", total=" + total +
                '}';
    }
}
