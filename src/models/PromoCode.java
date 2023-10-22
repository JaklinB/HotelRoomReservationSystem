package models;

public class PromoCode {
    private final String code;
    private final double discountPercentage;

    public PromoCode(String code, double discountPercentage) {
        this.code = code;
        this.discountPercentage = discountPercentage;
    }

    public String getCode() {
        return code;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
