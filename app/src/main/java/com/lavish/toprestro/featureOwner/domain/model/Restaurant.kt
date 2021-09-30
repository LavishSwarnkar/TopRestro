package com.lavish.toprestro.featureOwner.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lavish.toprestro.old.other.RESTAURANTS_TABLE

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (rId != other.rId) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (imageURL != other.imageURL) return false
        if (ownerEmail != other.ownerEmail) return false
        if (avgRating != other.avgRating) return false
        if (noOfRatings != other.noOfRatings) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rId.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (imageURL?.hashCode() ?: 0)
        result = 31 * result + (ownerEmail?.hashCode() ?: 0)
        result = 31 * result + avgRating.hashCode()
        result = 31 * result + noOfRatings
        return result
    }


}