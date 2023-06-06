package ir.ac.ut.ie.Entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.ac.ut.ie.DataBase;
import ir.ac.ut.ie.Exceptions.*;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name= "User")
public class User {
    @Id
    private String username;
    private String password;
    private String email;
    private Date birthDate;
    private String address;
    private Integer credit;
    @ElementCollection
    private Set<Integer> buyList = new HashSet<>();
    @ElementCollection
    private List<String> usedDiscounts = new ArrayList<>();
    @ManyToOne
    private Discount currentDiscount;


    public User(String email, String password, String username, String address, Date birthDate, Integer credit) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
    }

    public User() {}


    public void addToBuyList(Integer commodityId, int inStock) throws Exception {
        commodityAlreadyExists(commodityId);
        stockLimitError(inStock);
        buyList.add(commodityId);
    }

    public void commodityAlreadyExists(Integer commodityId) throws JsonProcessingException, CommodityAlreadyExists {
        if (buyList.contains(commodityId))
            throw new CommodityAlreadyExists();
    }

    public void stockLimitError(int inStock) throws Exception {
        if (0 >= inStock)
            throw new StockLimitError();
    }

    public void removeFromBuyList(Integer commodityId) throws Exception {
        if (!buyList.contains(commodityId))
            throw new CommodityNotFound();
        buyList.remove(commodityId);
    }

    public void addCredit(Integer credit) throws Exception {
        if (credit < 0)
            throw new InvalidCredit();
        this.credit += credit;
    }

    public Integer getBuyListPrice() throws Exception{
        int totalPrice = 0;
        for (Integer commodityId: buyList) {
            Commodity commodity = DataBase.getInstance().getCommodityById(commodityId);
            totalPrice += commodity.getPrice();
        }
        if (checkDiscount()) {
            Float tmpPrice = (float)(totalPrice);
            Float discount = (float)(currentDiscount.getDiscount());
            totalPrice = (int)(tmpPrice * (1 - discount/100));
        }
        return totalPrice;
    }
    private boolean checkDiscount() throws Exception {
        if (currentDiscount != null){
            if (usedDiscounts.contains(currentDiscount.getDiscountCode())){
                throw new DiscountAlreadyUsed();
            }
            return true;
        }
        return false;
    }

    public void payment() throws Exception  {
        int totalBuyListPrice = getBuyListPrice();
        if (totalBuyListPrice > credit)
            throw new NotEnoughCredit();
        for (Integer commodityId: buyList) {
            Commodity commodity = DataBase.getInstance().getCommodityById(commodityId);
            if (commodity.getInStock() < 1)
                throw new StockLimitError();
        }
        for (Integer commodityId: buyList) {
            Commodity commodity = DataBase.getInstance().getCommodityById(commodityId);
            commodity.reduceStock();
        }
        credit -= totalBuyListPrice;
        buyList.clear();
        if (currentDiscount != null){
            usedDiscounts.add(currentDiscount.getDiscountCode());
            currentDiscount = null;
        }
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getAddress() {
        return address;
    }
    public Integer getCredit() {
        return credit;
    }
    //    public String getNickname() {
//        return nickname;
//    }
//
    public Date getBirthDate() {
        return birthDate;
    }
    public Set<Integer> getBuyList() {
        return buyList;
    }

    public void setCurrentDiscount(Discount currentDiscount) throws Exception{
        if (usedDiscounts.contains(currentDiscount.getDiscountCode())){
            throw new DiscountAlreadyUsed();
        }
        this.currentDiscount = currentDiscount;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public void setCredit(Integer credit) {
        this.credit = credit;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
