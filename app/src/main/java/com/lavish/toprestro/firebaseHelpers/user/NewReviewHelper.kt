package com.lavish.toprestro.firebaseHelpers.user

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Review

class NewReviewHelper() {

    fun saveNewReview(restroId: String, review: Review, listener: OnCompleteListener<Void>){
        FirebaseFirestore.getInstance()
                .collection("restaurants/$restroId/reviews")
                .add(review)
                .addOnSuccessListener {
                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}