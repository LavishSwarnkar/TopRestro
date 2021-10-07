package com.lavish.toprestro.featureUser.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureUser.domain.repository.UserRepository

class GetReviewsFor(
    val userRepository: UserRepository
) {

    suspend operator fun invoke(restroId: String): List<Review> {
        return userRepository.getReviewsFor(restroId)
    }

}