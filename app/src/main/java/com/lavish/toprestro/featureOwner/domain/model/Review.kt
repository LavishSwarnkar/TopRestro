package com.lavish.toprestro.featureOwner.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Review(var id: String? = null,
        var timestamp: Date = Date(),

        var userName: String? = null,
        var userEmail: String? = null,

        var ownerEmail: String? = null,
        var restaurantId: String? = null,

        var starRating: Float? = 0f,
        var review: String? = null,
        var reply: String? = null,
        var restroName: String? = null) {

    constructor() : this(userName = null)

    fun formattedDate(): String {
        val sdf = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        return sdf.format(timestamp)
    }

    init {
        timestamp = Date()
    }
}