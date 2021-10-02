package com.lavish.toprestro.featureOwner.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review

//Cloud Repository to access Owner's Data
interface OwnerRepository {

    @Throws(FirestoreException::class)
    suspend fun getAllRestaurants(): List<Restaurant>

    suspend fun getReviews(): List<Review>

    suspend fun createNewRestaurant(restaurant: Restaurant)

    suspend fun replyReview(review: Review, reply: String)

}

class FirestoreException(message: String): Exception(message)