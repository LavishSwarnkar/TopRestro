package com.lavish.toprestro.featureOwner.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.FirestoreException
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
import kotlinx.coroutines.tasks.await

class OwnerRepositoryImpl(
    val prefsRepository: PrefsRepository,
    val restaurantRepository: RestaurantRepository
) : OwnerRepository {

    @Throws(FirestoreException::class)
    override suspend fun getAllRestaurants(): List<Restaurant> {
        val ownerProfile = prefsRepository.getProfile()

        try {
            val restaurants = FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerProfile.emailId)
                .get()
                .await()
                .toObjects(Restaurant::class.java)
            restaurantRepository.insertAllRestaurants(restaurants)
            return restaurants
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }

    @Throws(FirestoreException::class)
    override suspend fun getReviews(): List<Review> {
        val ownerProfile = prefsRepository.getProfile()

        try {
            val reviews = FirebaseFirestore.getInstance()
                .collectionGroup("reviews")
                .whereEqualTo("ownerEmail", ownerProfile.emailId)
                .whereEqualTo("reply", null)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(Review::class.java)
            return reviews
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