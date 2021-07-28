package com.lavish.toprestro.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review

class NewRestaurantHelper {

    fun fetch(ownerEmail: String, restaurant: Restaurant, listener: OnCompleteListener<List<Review>>){
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .add(restaurant)
                .addOnSuccessListener {
                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}