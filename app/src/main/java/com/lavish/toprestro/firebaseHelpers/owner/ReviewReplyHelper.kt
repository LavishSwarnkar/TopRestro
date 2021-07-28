package com.lavish.toprestro.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener

class ReviewReplyHelper {

    fun reply(restaurantId: String, reviewId: String, reply: String, listener: OnCompleteListener<Void>){
        FirebaseFirestore.getInstance()
                .document("restaurants/$restaurantId/reviews/$reviewId")
                .update("reply", reply)
                .addOnSuccessListener {
                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}