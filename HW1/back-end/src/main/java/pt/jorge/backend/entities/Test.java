package pt.jorge.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {

    private String millionPop;
    private int total;

    public Test() {

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
        return "Test{" +
                "millionPop='" + millionPop + '\'' +
                ", total=" + total +
                '}';
    }
}