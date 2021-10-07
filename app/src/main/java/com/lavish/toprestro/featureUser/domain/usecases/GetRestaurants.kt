package com.lavish.toprestro.featureUser.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureUser.domain.repository.UserRepository

class GetRestaurants(
    val userRepository: UserRepository
) {

    suspend operator fun invoke(): List<Restaurant> {
        return userRepository.getRestaurants()
    }

}