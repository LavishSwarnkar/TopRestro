package com.lavish.toprestro.featureOwner.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetRestaurants(
    private val restaurantRepository: RestaurantRepository
) {

    operator fun invoke(): Flow<List<Restaurant>> {
        return restaurantRepository.getAllRestaurants()
    }

}