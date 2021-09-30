package com.lavish.toprestro.featureOwner.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lavish.toprestro.featureOwner.domain.model.Profile
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.FirestoreException
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import kotlinx.coroutines.tasks.await

class OwnerRepositoryImpl(prefsRepository: PrefsRepository) : OwnerRepository {

    override val ownerProfile: Profile
        get() = Profile("", "")

    @Throws(FirestoreException::class)
    override suspend fun getAllRestaurants(): List<Restaurant> {
        try {
            return FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerProfile.emailId)
                .get()
                .await()
                .toObjects(Restaurant::class.java)
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }

    @Throws(FirestoreException::class)
    override suspend fun getReviews(): List<Review> {
        try {
            return FirebaseFirestore.getInstance()
                .collectionGroup("reviews")
                .whereEqualTo("ownerEmail", ownerProfile.emailId)
                .whereEqualTo("reply", null)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Review::class.java)
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }

    @Throws(FirestoreException::class)
    override suspend fun createNewRestaurant(restaurant: Restaurant) {
        try {
            FirebaseFirestore.getInstance()
                .collection("restaurants")
                .add(restaurant)
                .await()
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }

    @Throws(FirestoreException::class)
    override suspend fun replyReview(review: Review, reply: String) {
        try {
            FirebaseFirestore.getInstance()
                .document("restaurants/${review.restaurantId}/reviews/${review.id}")
                .update("reply", reply)
                .await()
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }
}