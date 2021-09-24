package com.lavish.toprestro.firebaseHelpers.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.user.ReviewsFetcher
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.TYPE_OWNER
import com.lavish.toprestro.other.TYPE_RESTAURANT

class DeleteHelper {

    fun delete(id: String, type: String, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
                .document("$type/$id")
                .delete()
                .addOnSuccessListener {

                    when (type) {
                        //if owner, delete all its restaurants
                        TYPE_OWNER -> deleteRestaurants(id, listener)

                        //if restro, delete all its reviews
                        TYPE_RESTAURANT -> deleteReviews(id, listener)

                        //Done!
                        else -> listener.onResult(null)
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

    private fun deleteReviews(restroId: String, listener: OnCompleteListener<Void?>) {
        ReviewsFetcher().fetch(restroId, object : OnCompleteListener<MutableList<Review>> {
            override fun onResult(t: MutableList<Review>) {
                val db = FirebaseFirestore.getInstance()
                val batch = db.batch()

                for(review in t)
                    batch.delete(db.document("restaurants/$restroId/reviews/${review.id}"))

                batch.commit().addOnSuccessListener {
                    listener.onResult(null)
                }.addOnFailureListener {
                    listener.onError(it.toString())
                }
            }

            override fun onError(e: String) {
                listener.onError(e)
            }
        })
    }

    private fun deleteRestaurants(ownerEmail: String, listener: OnCompleteListener<Void?>) {
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerEmail)
                .get()
                .addOnSuccessListener {
                    val batch = FirebaseFirestore.getInstance().batch()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists())
                            batch.delete(doc.reference)
                    }

                    batch.commit().addOnSuccessListener {
                        listener.onResult(null)
                    }.addOnFailureListener { e ->
                        listener.onError(e.toString())
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}