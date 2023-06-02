package ir.ac.ut.ie.Entities;

import ir.ac.ut.ie.Exceptions.InvalidCommand;
import ir.ac.ut.ie.Exceptions.InvalidRateScore;

public class Discount {
    private String discountCode;
    private Integer discount;

//    public Discount(String discountCode, Integer discount) {
//        this.discountCode = discountCode;
//        this.discount = discount;
//    }

    public void checkForInvalidCommand() throws InvalidCommand {
        if (discountCode == null || discount == null)
            throw new InvalidCommand();
    }

    public String getDiscountCode() {
        return discountCode;
    }
    public Integer getDiscount() {
        return discount;
    }
}
