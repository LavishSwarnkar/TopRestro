package com.lavish.toprestro.featureOwner.data.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

@Database(entities = [Restaurant::class], version = 1)
abstract class RestaurantDatabase: RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        const val DATABASE_NAME = "restaurants.db"
    }
}

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllRestaurants(restaurants: List<Restaurant>)

    @Insert(onConflict = REPLACE)
    suspend fun insertRestaurant(restaurants: Restaurant)

}