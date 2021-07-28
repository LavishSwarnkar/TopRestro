package com.lavish.toprestro.models;

public class Restaurant {

    //TODO : Image upload & compress

    public String id, name, imageURL, ownerEmail;
    public float avgRating;
    public int noOfRatings;

    public Restaurant() {
    }

    public Restaurant(String name, String imageURL, float avgRating, int noOfRatings) {
        this.name = name;
        this.imageURL = imageURL;
        this.avgRating = avgRating;
        this.noOfRatings = noOfRatings;
    }

    public Restaurant(String name, String ownerEmail) {
        this.name = name;
        this.ownerEmail = ownerEmail;
    }
}
