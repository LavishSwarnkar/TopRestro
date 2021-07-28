package com.lavish.toprestro.firebaseHelpers.user

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review

class ReviewsFetcher() {

    fun fetch(restroId: String, listener: OnCompleteListener<MutableList<Review>>){
        FirebaseFirestore.getInstance()
                .collection("restaurants/$restroId/reviews")
                .get()
                .addOnSuccessListener {
                    val reviews = mutableListOf<Review>()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists()) {
                            val review = doc.toObject(Review::class.java)!!
                            review.id = doc.id
                            reviews.add(review)
                        }
                    }

                    listener.onResult(reviews)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}