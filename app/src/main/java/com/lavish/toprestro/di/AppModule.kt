package com.lavish.toprestro.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.lavish.toprestro.featureOwner.data.firestore.OwnerRepositoryImpl
import com.lavish.toprestro.featureOwner.data.prefs.PrefsRepositoryImpl
import com.lavish.toprestro.featureOwner.data.room.RestaurantDatabase
import com.lavish.toprestro.featureOwner.data.room.RestaurantRepositoryImpl
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import com.lavish.toprestro.featureOwner.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRestaurantDatabase(app: Application): RestaurantDatabase {
        return Room.databaseBuilder(
            app,
            RestaurantDatabase::class.java,
            RestaurantDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePrefsRepository(@ApplicationContext context: Context): PrefsRepository {
        val datastore = PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("settings")
            }
        )
        return PrefsRepositoryImpl(datastore)
    }

    @Provides
    @Singleton
    fun provideOwnerRepository(prefsRepository: PrefsRepository): OwnerRepository {
        return OwnerRepositoryImpl(prefsRepository)
    }

    @Provides
    @Singleton
    fun provideRestaurantRepository(db: RestaurantDatabase): RestaurantRepository {
        return RestaurantRepositoryImpl(db.restaurantDao())
    }

    @Provides
    @Singleton
    fun provideOwnerUseCases(
        ownerRepository: OwnerRepository,
        restaurantRepository: RestaurantRepository
    ): OwnerUseCases {

        return OwnerUseCases(
            CreateNewRestaurant(ownerRepository, restaurantRepository),
            GetRestaurants(restaurantRepository),
            GetReviews(ownerRepository),
            ReplyReview(ownerRepository)
        )
    }

}