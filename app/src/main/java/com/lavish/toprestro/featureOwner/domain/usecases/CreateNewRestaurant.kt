package com.lavish.toprestro.featureOwner.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository

class CreateNewRestaurant(
    private val ownerRepository: OwnerRepository,
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(restaurant: Restaurant) {
        ownerRepository.createNewRestaurant(restaurant)
        restaurantRepository.insertRestaurant(restaurant)
    }

}