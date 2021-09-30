package com.lavish.toprestro.old.firebaseHelpers.admin

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener

class ReviewActionsHelper {

    fun deleteReview(restaurantId: String, reviewId: String, newAvgRating: Float, listener: OnCompleteListener<Void?>){
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()

        batch.delete(db.document("restaurants/$restaurantId/reviews/$reviewId"))
        batch.update(db.document("restaurants/$restaurantId"),
        mapOf("noOfRatings" to FieldValue.increment(-1),
                "avgRating" to newAvgRating))

        batch.commit()
                .addOnSuccessListener {
                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

    fun editReply(restaurantId: String, reviewId: String, newReply: String, listener: OnCompleteListener<Void?>){
        val map = mapOf<String, Any>("reply" to newReply)
        editReview(restaurantId, reviewId, map, listener)
    }

    fun editReview(restaurantId: String, reviewId: String, newReview: String, listener: OnCompleteListener<Void?>){
        val map = mapOf<String, Any>("review" to newReview)
        editReview(restaurantId, reviewId, map, listener)
    }

    fun deleteReply(restaurantId: String, reviewId: String, listener: OnCompleteListener<Void?>){
        val map = mapOf<String, Any?>("reply" to null)
        editReview(restaurantId, reviewId, map, listener)
    }

    private fun editReview(restaurantId: String, reviewId: String, map: Map<String, Any?>, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
                .document("restaurants/$restaurantId/reviews/$reviewId")
                .update(map)
                .addOnSuccessListener {
                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}