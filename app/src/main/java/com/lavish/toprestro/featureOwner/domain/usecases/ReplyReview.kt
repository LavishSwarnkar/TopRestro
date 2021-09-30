package com.lavish.toprestro.featureOwner.domain.usecases

import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository

class ReplyReview(
    private val ownerRepository: OwnerRepository
) {

    suspend operator fun invoke(
        review: Review,
        reply: String
    ) {
        ownerRepository.replyReview(review, reply)
    }

}