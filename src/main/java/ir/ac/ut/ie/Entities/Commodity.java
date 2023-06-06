package ir.ac.ut.ie.Entities;

import ir.ac.ut.ie.Exceptions.InvalidCommand;
import ir.ac.ut.ie.Exceptions.StockLimitError;

import java.util.*;

public class Commodity {
    //    private Integer id;
//    private String name;
//    private String summary;
//    private String releaseDate;
//    private String director;
//    private List<String> writers;
//    private List<String> genres;
//    private List<Integer> cast;
//    private List<String> castName;
//    private Float imdbRate;
//    private Integer duration;
//    private Integer ageLimit;
//    public float rating;
//    private int ratingCount;
//    private Map<Integer, Comment> comments;
//    private Map<String, Integer> rates;
//    int score;
    private Integer id;
    private String name;
    private Integer providerId;
    private String providerName;
    private Integer price;
    private List<String> categories;
    private Float rating;
    private Integer inStock;
    private int ratingCount;
    private Map<Integer, Comment> comments;
    private Map<String, Integer> rates;

    private String releaseDate;

    int score;

    public void initialValues() {
        ratingCount = 1;
        comments = new HashMap<>();
        rates = new HashMap<>();
    }

    public void update(Commodity updatedCommodity) {
        name = updatedCommodity.getName();
        providerId = updatedCommodity.getProviderId();
        price = updatedCommodity.getPrice();
        releaseDate = updatedCommodity.getReleaseDate();
        categories = updatedCommodity.getCategories();
        rating = updatedCommodity.getRating();
        inStock = updatedCommodity.getInStock();
    }

    public void addComment(Comment comment, Integer commentId) {
        comment.initialValues(commentId);
        comments.put(commentId, comment);
    }

    public void addRate(Rate rate) {
        if (rates.containsKey(rate.getUsername()))
            rating = (rating * ratingCount - rates.get(rate.getUsername()) + rate.getScore()) / ratingCount;
        else {
            rating = (rating * ratingCount + rate.getScore()) / (ratingCount + 1);
            ratingCount += 1;
        }
        rates.put(rate.getUsername(), (int) rate.getScore());
    }

    public boolean categoryMatch(String genre) {
        for (String curCategory : getCategories())
            if (curCategory.equals(genre))
                return true;
        return false;
    }

    public void checkForInvalidCommand() throws InvalidCommand {
        if (id == null || name == null || providerId == null || price == null || categories == null || rating == null
                || inStock == null)
            throw new InvalidCommand();
    }
    public void reduceStock() throws Exception {
        if (inStock < 1)
            throw new StockLimitError();
        inStock -= 1;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public String getSummary() {
//        return summary;
//    }

    public Integer getProviderId() {
        return providerId;
    }
    public String getProviderName() {
        return providerName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getPrice() {
        return price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Integer getInStock() {
        return inStock;
    }
//    public String getReleaseDate() {
//        return releaseDate;
//    }

//    public String getDirector() {
//        return director;
//    }

//    public List<String> getWriters() {
//        return writers;
//    }

//    public List<String> getGenres() {
//        return genres;
//    }

//    public List<Integer> getCast() {
//        return cast;
//    }

//    public Float getImdbRate() {
//        return imdbRate;
//    }
//
//    public Integer getDuration() {
//        return duration;
//    }
//
//    public Integer getAgeLimit() {
//        return ageLimit;
//    }

    public float getRating() {
        return rating;
    }

//    public int getRatingCount() {
//        return ratingCount;
//    }

    public Map<Integer, Comment> getComments() {
        return comments;
    }

//    public Map<String, Integer> getRates() {
//        return rates;
//    }

    //    public void setCastName(List<String> castName) {
//        this.castName = castName;
//    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

//    public List<String> getCastName() {
//        return castName;
//    }

//    public int getScore() {
//        return score;
//    }

    //    public void setScore(int score) {
//        this.score = score;
//    }
    public void setScore(int score) {
        this.score = score;
    }
}

