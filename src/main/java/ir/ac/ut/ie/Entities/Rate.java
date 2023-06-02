package ir.ac.ut.ie.Entities;

import ir.ac.ut.ie.Exceptions.InvalidRateScore;

public class Rate {
    private final String username;
    private final Integer commodityId;
    private final float score;

    public Rate(String username, Integer commodityId, float score) {
        this.username = username;
        this.commodityId = commodityId;
        this.score = score;
    }

    public void hasError() throws Exception {
        if ((((int) score != score) || (score < 1 || score > 10)))
            throw new InvalidRateScore();
    }

    public String getUsername() {
        return username;
    }
    public Integer getCommodityId() {
        return commodityId;
    }
    public float getScore() {
        return score;
    }
}
