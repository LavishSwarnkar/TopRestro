package com.lavish.toprestro.featureUser.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review

interface UserRepository {

    suspend fun getRestaurants(): List<Restaurant>

    suspend fun getReviewsFor(restroId: String): List<Review>

    suspend fun newReview(restroId: String, review: Review, newAvgRating: Float)

}