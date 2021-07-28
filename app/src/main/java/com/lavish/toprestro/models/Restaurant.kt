package com.lavish.toprestro.models

data class Restaurant(
        var id: String? = null,
        var name: String? = null,
        var imageURL: String? = null,
        var ownerEmail: String? = null,
        var avgRating: Float = 0f,
        var noOfRatings: Int = 0) {

    constructor() : this(name = null)

}