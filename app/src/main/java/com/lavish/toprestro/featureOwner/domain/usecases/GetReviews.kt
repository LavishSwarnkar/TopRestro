package com.lavish.toprestro.featureOwner.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository

class GetReviews(
    private val ownerRepository: OwnerRepository
) {

    suspend operator fun invoke(): List<Review> {
        return ownerRepository.getReviews()
    }

}