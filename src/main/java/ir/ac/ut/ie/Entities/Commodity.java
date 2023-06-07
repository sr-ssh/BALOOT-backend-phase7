package ir.ac.ut.ie.Entities;

import ir.ac.ut.ie.Exceptions.InvalidCommand;
import ir.ac.ut.ie.Exceptions.StockLimitError;
import ir.ac.ut.ie.Repository.RateRepository;
import javax.persistence.*;

import java.util.*;

@Entity
@Table(name= "Commodity")
public class Commodity {
    @Id
    @Column(name = "commodity_id")
    private Integer id;
    private String name;
    private Integer providerId;
    private String providerName;
    private Integer price;
    @ElementCollection
    private List<String> categories;
    private Float rating;
    private Integer inStock;
    private int ratingCount;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comment> comments;
    private String releaseDate;
    int score;
    @Column(name = "image", length = 2048)
    private String image;

    public void initialValues() {
        ratingCount = 1;
        comments = new ArrayList<>();
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

    public void addComment(Comment comment) {
        comment.initialValues();
        comments.add(comment);
    }

    public void addRate(Rate rate, RateRepository rateRepository) {
        Rate rateOb = rateRepository.findRateByUsernameAndCommodityId(rate.getUsername(), rate.getCommodityId());

        if (rateOb != null) {
            rating = (rating * ratingCount - rateOb.getScore() + rate.getScore()) / ratingCount;
            rateRepository.delete(rateOb);
        }        else {
            rating = (rating * ratingCount + rate.getScore()) / (ratingCount + 1);
            ratingCount += 1;
        }
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

    public List<Comment> getComments() {
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
    public String getImage() {
        return image;
    }
}

