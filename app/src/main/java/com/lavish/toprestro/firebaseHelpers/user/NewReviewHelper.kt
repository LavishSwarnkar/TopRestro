package com.lavish.toprestro.firebaseHelpers.user

import android.content.Context
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Review

class NewReviewHelper() {

    fun saveNewReview(restroId: String, newAvgRating: Float, review: Review, listener: OnCompleteListener<Void?>){
        val db = FirebaseFirestore.getInstance()

        db.collection("restaurants/$restroId/reviews")
                .add(review)
                .addOnSuccessListener {

                    //Update avgRating in restro doc
                    db.document("restaurants/$restroId")
                            .update(mapOf(
                                    "noOfRatings" to FieldValue.increment(1),
                                    "avgRating" to newAvgRating
                            )).addOnSuccessListener {
                                listener.onResult(null)
                            }
                            .addOnFailureListener {
                                listener.onError(it.toString())
                            }
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}