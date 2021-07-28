package com.lavish.toprestro.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Review

class NewReviewsFetcher() {

    fun fetch(ownerEmail: String, listener: OnCompleteListener<List<Review>>){
        FirebaseFirestore.getInstance()
                .collectionGroup("reviews")
                .whereEqualTo("ownerEmail", ownerEmail)
                .whereEqualTo("reply", null)
                .orderBy("timestamp", Query.Direction.DESCENDING)
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