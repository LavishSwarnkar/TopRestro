package com.lavish.toprestro.featureOwner.data.room

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class RestaurantRepositoryImpl(
    val dao: RestaurantDao
): RestaurantRepository {

    override fun getAllRestaurants(): Flow<List<Restaurant>> {
        return dao.getAllRestaurants()
    }

    override suspend fun insertRestaurant(restaurant: Restaurant) {
        dao.insertRestaurant(restaurant)
    }

    override suspend fun insertAllRestaurants(restaurants: List<Restaurant>) {
        dao.insertAllRestaurants(restaurants)
    }
}