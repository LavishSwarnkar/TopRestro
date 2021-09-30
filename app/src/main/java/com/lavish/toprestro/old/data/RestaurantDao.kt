package com.lavish.toprestro.old.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.other.RESTAURANTS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Insert
    suspend fun insertRestaurant(restaurant: Restaurant)

    @Insert
    suspend fun insertAllRestaurants(restaurants: List<Restaurant>)

    @Query("SELECT * FROM $RESTAURANTS_TABLE")
    fun getRestaurants(): Flow<List<Restaurant>>

}