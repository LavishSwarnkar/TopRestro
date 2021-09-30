package com.lavish.toprestro.featureOwner.presentation.ownerHome

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review

sealed class OwnerHomeEvent {
    object Refresh: OwnerHomeEvent()
    class CreateNewRestaurant(val restaurant: Restaurant): OwnerHomeEvent()
    class ReplyReview(val review: Review, val reply: String): OwnerHomeEvent()
    object Logout: OwnerHomeEvent()
}