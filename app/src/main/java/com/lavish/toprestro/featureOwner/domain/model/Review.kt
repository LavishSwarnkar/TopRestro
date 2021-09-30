package com.lavish.toprestro.featureOwner.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Review(var id: String,
        var timestamp: Date,

        var userName: String,
        var userEmail: String,

        var ownerEmail: String,
        var restaurantId: String,

        var starRating: Float,
        var review: String,
        var reply: String,
        var restroName: String) {

    fun formattedDate(): String {
        val sdf = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        return sdf.format(timestamp)
    }

}