package ir.ac.ut.ie.Entities;

import ir.ac.ut.ie.Exceptions.InvalidRateScore;

import javax.persistence.*;

@Entity
@Table(name= "Rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String username;
    private Integer commodityId;
    private float score;
    public Rate(String username, Integer commodityId, float score) {
        this.username = username;
        this.commodityId = commodityId;
        this.score = score;
    }
    public Rate() {}

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
