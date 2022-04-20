package pt.jorge.backend.entities.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

    @JsonProperty("1M_pop")
    private String millionPop;
    private String total;

    public Test() {

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
        return "Test{" +
                "millionPop=" + millionPop +
                ", total=" + total +
                '}';
    }
}