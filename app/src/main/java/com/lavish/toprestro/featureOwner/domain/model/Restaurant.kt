package com.lavish.toprestro.featureOwner.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Restaurant(
        @PrimaryKey
        var id: String,
        val name: String,
        val imageURL: String,
        val ownerEmail: String,
        var avgRating: Float = 0f,
        var noOfRatings: Int = 0) {

        constructor(): this(
                "",
                "",
                "",
                "",
                0f,
                0
        )

        override fun toString(): String = name
}