package com.lavish.toprestro.featureUser.domain.usecases

data class UserUseCases(
    val getRestaurants: GetRestaurants,
    val getReviewsFor: GetReviewsFor,
    val newReview: NewReview,
    val getUserProfile: GetUserProfile
)