package com.lavish.toprestro.featureOwner.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

//Local repository to access Owner's Restaurants
interface RestaurantRepository {

    fun getAllRestaurants(): Flow<List<Restaurant>>

    suspend fun insertRestaurant(restaurant: Restaurant)

    suspend fun insertAllRestaurants(restaurants: List<Restaurant>)

}