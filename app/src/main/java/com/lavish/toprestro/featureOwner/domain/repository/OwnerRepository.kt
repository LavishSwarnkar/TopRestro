package com.lavish.toprestro.featureOwner.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review

//Cloud Repository to access Owner's Data
interface OwnerRepository {

    @Throws(FirestoreException::class)
    suspend fun getAllRestaurantsOf(ownerEmail: String): List<Restaurant>

    suspend fun getAllPendingReviews(ownerEmail: String): List<Review>

    suspend fun createNewRestaurant(restaurant: Restaurant): String

    suspend fun replyReview(review: Review, reply: String)

}

class FirestoreException(message: String): Exception(message)