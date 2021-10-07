package com.lavish.toprestro.featureUser.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Profile
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository

class GetUserProfile(
    val prefsRepository: PrefsRepository
) {

    suspend operator fun invoke(): Profile{
        return prefsRepository.getProfile()
    }

}