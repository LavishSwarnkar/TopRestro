package com.lavish.toprestro.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lavish.toprestro.other.RESTAURANTS_TABLE

@Entity(tableName = RESTAURANTS_TABLE)
data class Restaurant(
        @PrimaryKey
        var rId: String = "",
        var id: String? = null,
        var name: String? = null,
        var imageURL: String? = null,
        var ownerEmail: String? = null,
        var avgRating: Float = 0f,
        var noOfRatings: Int = 0) {

    constructor() : this("")

    override fun toString(): String {
        return "Restaurant(name=$name)"
    }


}