package com.example.finalproject.Domain;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable {
    private String title;
    private String description;
    private String imageUrl;
    private double price;
    private double oldPrice;
    private int review;
    private double rating;
    private int NumberinCart;

    private int url;
    private int Id;
    private String size;
    private String color;
    private String address;
    private int number;
    public ItemsDomain() {
    }
    public ItemsDomain(String title, double price, double oldprice, double rating, int imageUrl, String description) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.oldPrice = oldprice;
        this.rating = rating;
        this.url = imageUrl;
    }
    public ItemsDomain(int imageResourceId, String popularItem, int popularReview, String s, double popularPrice, double popularRating, double popularOldPrice) {
        this.url = imageResourceId;
        this.title = popularItem;
        this.review = popularReview;
        this.description=s;
        this.price = popularPrice;
        this.rating =popularRating;
        this.oldPrice = popularOldPrice;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberinCart() {
        return NumberinCart;
    }

    public void setNumberinCart(int numberinCart) {
        NumberinCart = numberinCart;
    }

    public int getId(){
        return Id;
    }
    public void setId(int Id){
        this.Id = Id;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}