package com.lavish.toprestro.featureLogin.domain.usecases

import com.lavish.toprestro.featureLogin.domain.repository.LoginRepository
import com.lavish.toprestro.featureOwner.domain.model.Profile
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository

class CreateAccount(
    val loginRepository: LoginRepository,
    val prefsRepository: PrefsRepository
) {

    suspend operator fun invoke(userType: String, profile: Profile) {
        loginRepository.createAccount(userType, profile)
        prefsRepository.saveProfile(profile, userType)
    }

}