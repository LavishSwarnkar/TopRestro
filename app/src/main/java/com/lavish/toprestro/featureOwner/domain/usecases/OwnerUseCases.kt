package com.lavish.toprestro.featureOwner.domain.usecases

data class OwnerUseCases(
    val createNewRestaurant: CreateNewRestaurant,
    val getRestaurants: GetRestaurants,
    val getReviews: GetReviews,
    val replyReview: ReplyReview
)