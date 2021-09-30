package com.lavish.toprestro.old.di

import android.content.Context
import com.lavish.toprestro.old.data.RestaurantDao
import com.lavish.toprestro.old.data.RestaurantDatabase
import com.lavish.toprestro.old.other.NetworkStatusHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {



    @Singleton
    @Provides
    fun getRestaurantsDao(@ApplicationContext context: Context): RestaurantDao {
        return RestaurantDatabase.getDatabase(context = context).restaurantDao()
    }

    @Singleton
    @Provides
    fun getNetworkStatusHelper(@ApplicationContext context: Context): NetworkStatusHelper {
        return NetworkStatusHelper(context)
    }

}