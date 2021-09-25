package com.lavish.toprestro.di

import android.content.Context
import com.lavish.toprestro.data.RestaurantDao
import com.lavish.toprestro.data.RestaurantDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AnotherAppModule {

    @Singleton
    @Provides
    fun getRestaurantsDao(@ApplicationContext context: Context): RestaurantDao {
        return RestaurantDatabase.getDatabase(context = context).restaurantDao()
    }

}