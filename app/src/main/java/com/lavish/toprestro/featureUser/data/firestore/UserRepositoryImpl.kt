package com.lavish.toprestro.featureUser.data.firestore

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureUser.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {

    override suspend fun getRestaurants(): List<Restaurant> {
        return FirebaseFirestore.getInstance()
            .collection("restaurants")
            .get()
            .await()
            .filter { it != null && it.exists() }
            .map { doc ->
                val restro = doc.toObject(Restaurant::class.java)
                restro.id = doc.id
                restro
            }
    }

    override suspend fun getReviewsFor(restroId: String): List<Review> {
        return FirebaseFirestore.getInstance()
            .collection("restaurants/$restroId/reviews")
            .get()
            .await()
            .filter { it != null && it.exists() }
            .map { doc ->
                val review = doc.toObject(Review::class.java)
                review.id = doc.id
                review
            }
    }

    override suspend fun newReview(restroId: String, review: Review, newAvgRating: Float) {
        val db = FirebaseFirestore.getInstance()

        db.collection("restaurants/$restroId/reviews")
            .add(review)
            .await()

        db.document("restaurants/$restroId")
            .update(mapOf(
                "noOfRatings" to FieldValue.increment(1),
                "avgRating" to newAvgRating
            ))
            .await()
    }
}