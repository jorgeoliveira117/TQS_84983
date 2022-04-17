package pt.jorge.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Death {

    private String newCases;
    private String millionPop;
    private int total;

    public Death(){
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Death{" +
                "newCases='" + newCases + '\'' +
                ", millionPop='" + millionPop + '\'' +
                ", total=" + total +
                '}';
    }
}
