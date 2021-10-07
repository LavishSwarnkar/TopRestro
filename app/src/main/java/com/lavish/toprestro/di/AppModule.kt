package com.lavish.toprestro.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.lavish.toprestro.featureLogin.data.repository.LoginRepositoryImpl
import com.lavish.toprestro.featureLogin.domain.repository.LoginRepository
import com.lavish.toprestro.featureLogin.domain.usecases.CreateAccount
import com.lavish.toprestro.featureLogin.domain.usecases.Login
import com.lavish.toprestro.featureLogin.domain.usecases.LoginUseCases
import com.lavish.toprestro.featureOwner.data.firestore.OwnerRepositoryImpl
import com.lavish.toprestro.featureOwner.data.prefs.PrefsRepositoryImpl
import com.lavish.toprestro.featureOwner.data.room.RestaurantDatabase
import com.lavish.toprestro.featureOwner.data.room.RestaurantRepositoryImpl
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import com.lavish.toprestro.featureOwner.domain.usecases.*
import com.lavish.toprestro.featureUser.data.firestore.UserRepositoryImpl
import com.lavish.toprestro.featureUser.domain.repository.UserRepository
import com.lavish.toprestro.featureUser.domain.usecases.GetReviewsFor
import com.lavish.toprestro.featureUser.domain.usecases.GetUserProfile
import com.lavish.toprestro.featureUser.domain.usecases.NewReview
import com.lavish.toprestro.featureUser.domain.usecases.UserUseCases
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
    fun provideOwnerRepository(): OwnerRepository = OwnerRepositoryImpl()

    @Provides
    @Singleton
    fun provideRestaurantRepository(db: RestaurantDatabase): RestaurantRepository =
        RestaurantRepositoryImpl(db.restaurantDao())

    @Provides
    @Singleton
    fun provideLoginRepository(): LoginRepository = LoginRepositoryImpl()

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepositoryImpl()

    @Provides
    @Singleton
    fun provideLoginUseCases(
        loginRepository: LoginRepository,
        restaurantRepository: RestaurantRepository,
        ownerRepository: OwnerRepository,
        prefsRepository: PrefsRepository
    ): LoginUseCases = LoginUseCases(
        Login(loginRepository, restaurantRepository, ownerRepository, prefsRepository),
        CreateAccount(loginRepository, prefsRepository)
    )

    @Provides
    @Singleton
    fun provideOwnerUseCases(
        prefsRepository: PrefsRepository,
        ownerRepository: OwnerRepository,
        restaurantRepository: RestaurantRepository
    ): OwnerUseCases {

        return OwnerUseCases(
            CreateNewRestaurant(ownerRepository, restaurantRepository),
            GetRestaurants(restaurantRepository),
            GetReviews(prefsRepository, ownerRepository),
            ReplyReview(ownerRepository)
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        prefsRepository: PrefsRepository,
        userRepository: UserRepository
    ): UserUseCases {
        return UserUseCases(
            com.lavish.toprestro.featureUser.domain.usecases.GetRestaurants(userRepository),
            GetReviewsFor(userRepository),
            NewReview(userRepository),
            GetUserProfile(prefsRepository)
        )
    }

}