package com.lavish.toprestro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lavish.toprestro.models.Restaurant

@Database(entities = arrayOf(Restaurant::class), version = 1)
abstract class RestaurantDatabase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        var INSTANCE: RestaurantDatabase? = null

        fun getDatabase(context: Context): RestaurantDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    RestaurantDatabase::class.java,
                    "db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}