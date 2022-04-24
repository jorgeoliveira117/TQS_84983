package pt.jorge.backend.entities;

public class CacheStats {

    private String name;
    private long timeToLive;
    private int totalKeys;
    private int successfulHits;
    private int missedHits;

    public CacheStats(){}

    public CacheStats(String name, long timeToLive, int totalKeys, int successfulHits, int missedHits) {
        this.timeToLive = timeToLive;
        this.totalKeys = totalKeys;
        this.successfulHits = successfulHits;
        this.missedHits = missedHits;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getTotalKeys() {
        return totalKeys;
    }

    public void setTotalKeys(int totalKeys) {
        this.totalKeys = totalKeys;
    }

    public int getSuccessfulHits() {
        return successfulHits;
    }

    public void setSuccessfulHits(int successfulHits) {
        this.successfulHits = successfulHits;
    }

    public int getMissedHits() {
        return missedHits;
    }

    public void setMissedHits(int missedHits) {
        this.missedHits = missedHits;
    }

    @Override
    public String toString() {
        return "CacheStats{" +
                "name='" + name + '\'' +
                ", totalKeys=" + totalKeys +
                ", successfulHits=" + successfulHits +
                ", missedHits=" + missedHits +
                ", timeToLive=" + timeToLive  +
                '}';
    }
}
