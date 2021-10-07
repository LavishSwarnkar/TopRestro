package com.lavish.toprestro.featureUser.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureUser.domain.repository.UserRepository

class NewReview(
    val userRepository: UserRepository
) {

    suspend operator fun invoke(restroId: String, review: Review, newAvgRating: Float) {
        userRepository.newReview(restroId, review, newAvgRating)
    }

}