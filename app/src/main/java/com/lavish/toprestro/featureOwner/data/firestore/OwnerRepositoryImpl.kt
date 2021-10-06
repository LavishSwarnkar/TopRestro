package com.lavish.toprestro.featureOwner.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.repository.FirestoreException
import com.lavish.toprestro.featureOwner.domain.repository.OwnerRepository
import kotlinx.coroutines.tasks.await

class OwnerRepositoryImpl : OwnerRepository {

    @Throws(FirestoreException::class)
    override suspend fun getAllRestaurantsOf(ownerEmail: String): List<Restaurant> {
        try {
            val restaurants = FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerEmail)
                .get()
                .await()
                .filter { doc ->
                    doc != null && doc.exists()
                }
                .map { doc ->
                    val restaurant = doc.toObject(Restaurant::class.java)
                    restaurant.id = doc.id
                    restaurant
                }

            return restaurants
        } catch (e: Exception) {
            throw FirestoreException(e.message.toString())
        }
    }

    @Throws(FirestoreException::class)
    override suspend fun getAllPendingReviews(ownerEmail: String): List<Review> {
        try {
            return FirebaseFirestore.getInstance()
                .collectionGroup("reviews")
                .whereEqualTo("ownerEmail", ownerEmail)
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
    override suspend fun createNewRestaurant(restaurant: Restaurant): String {
        try {
            val doc = FirebaseFirestore.getInstance()
                .collection("restaurants")
                .add(restaurant)
                .await()
            return doc.id
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