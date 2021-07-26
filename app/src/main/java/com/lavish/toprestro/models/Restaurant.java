package com.lavish.toprestro.models;

public class Restaurant {

    //TODO : Image upload & compress

    public String id, name, imageURL, ownerEmail;
    public float avgRating;
    public int noOfRatings;

    public Restaurant() {
    }

    public Restaurant(String name, String ownerEmail) {
        this.name = name;
        this.ownerEmail = ownerEmail;
    }
}
