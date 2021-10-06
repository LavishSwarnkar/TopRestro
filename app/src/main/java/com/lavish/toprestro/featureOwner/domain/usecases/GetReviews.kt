package com.lavish.toprestro.featureOwner.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository

class GetReviews(
    private val prefsRepository: PrefsRepository,
    private val ownerRepository: OwnerRepository
) {

    suspend operator fun invoke(): List<Review> {
        val ownerProfile = prefsRepository.getProfile()
        return ownerRepository.getAllPendingReviews(ownerProfile.emailId)
    }

}