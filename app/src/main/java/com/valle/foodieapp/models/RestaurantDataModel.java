package com.valle.foodieapp.models;

public class RestaurantDataModel {

    private String image;
    private String restaurantName;
    private String price;
    private String discountPercentage;
    private String promoCode1;
    private String promoCode2;
    private String category;
    private String rating;


    public RestaurantDataModel(String image, String restaurantName, String price, String discountPercentage, String promoCode1, String promoCode2, String category, String rating) {
        this.image = image;
        this.restaurantName = restaurantName;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.promoCode1 = promoCode1;
        this.promoCode2 = promoCode2;
        this.category = category;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public String getPromoCode1() {
        return promoCode1;
    }

    public String getPromoCode2() {
        return promoCode2;
    }

    public String getCategory() {
        return category;
    }

    public String getRating() {
        return rating;
    }
}
