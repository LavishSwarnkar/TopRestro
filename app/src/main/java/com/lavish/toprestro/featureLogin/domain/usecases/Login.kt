package com.lavish.toprestro.featureLogin.domain.usecases

import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.featureLogin.domain.repository.LoginRepository
import com.lavish.toprestro.featureLogin.domain.repository.Result
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import com.lavish.toprestro.other.TYPE_ADMIN
import com.lavish.toprestro.other.TYPE_OWNER

class Login(
    val loginRepository: LoginRepository,
    val restaurantRepository: RestaurantRepository,
    val ownerRepository: OwnerRepository,
    val prefsRepository: PrefsRepository
) {

    suspend operator fun invoke(userType: String, email: String): LoginResult {

        when (val accountResult = loginRepository.checkAccountExists(userType, email)) {

            //Account exists
            is Result.Success -> {
                val profile = accountResult.t

                //Save profile
                prefsRepository.saveProfile(profile, userType)

                //If owner, fetch & save all restaurants
                if(userType == TYPE_OWNER) {
                    val restaurants = ownerRepository.getAllRestaurantsOf(email)
                    restaurantRepository.insertAllRestaurants(restaurants)
                }

                return LoginResult.LoginSuccessful(profile.name)
            }

            //Account does not exist
            is Result.Failure -> {
                //Admin does not exist
                if(userType == TYPE_ADMIN) {
                    FirebaseAuth.getInstance().signOut()
                    return LoginResult.AdminAccessDenied
                }

                //Account not yet created
                return LoginResult.AccountDoesNotExist
            }
        }
    }

}

sealed class LoginResult {
    data class LoginSuccessful(val userName: String): LoginResult()
    object AdminAccessDenied: LoginResult()
    object AccountDoesNotExist: LoginResult()
}