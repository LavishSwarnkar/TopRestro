package com.lavish.toprestro.firebaseHelpers.admin

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener

class ReviewActionsHelper {

    fun deleteReview(restaurantId: String, reviewId: String, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
                .document("restaurants/$restaurantId/reviews/$reviewId")
                .delete()
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
        //TODO : Remove rating from restro doc
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