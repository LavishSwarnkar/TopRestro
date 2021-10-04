package com.lavish.toprestro.featureOwner.domain.model

import com.lavish.compose.ui.theme.Stars2
import com.lavish.compose.ui.theme.Stars3
import com.lavish.compose.ui.theme.Stars4
import com.lavish.compose.ui.theme.Stars5
import java.text.SimpleDateFormat
import java.util.*

data class Review(var id: String?,
        var timestamp: Date,

        var userName: String,
        var userEmail: String,

        var ownerEmail: String,
        var restaurantId: String,

        var starRating: Float,
        var review: String,
        var reply: String?,
        var restroName: String) {

    constructor(): this(
        "",
        Date(),
        "",
        "",
        "",
        "",
        0f,
        "",
        "",
        ""
    )

    fun formattedDate(): String {
        val sdf = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        return sdf.format(timestamp)
    }

    fun colorForRating() = when {
        starRating > 4 -> Stars5
        starRating > 3 -> Stars4
        starRating > 2 -> Stars3
        else -> Stars2
    }


}