package com.lavish.toprestro.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Review {

    public String id, userName, review, reply, ownerEmail;
    public Date timestamp;
    public int starRating;

    public Review(String userName, String ownerEmail, String review, int starRating) {
        this.userName = userName;
        this.review = review;
        this.ownerEmail = ownerEmail;
        this.starRating = starRating;

        timestamp = Calendar.getInstance().getTime();
    }

    public String formattedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        return sdf.format(timestamp);
    }

}
