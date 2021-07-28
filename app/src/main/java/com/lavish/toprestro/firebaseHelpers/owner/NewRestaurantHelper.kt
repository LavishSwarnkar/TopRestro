package com.lavish.toprestro.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant

class NewRestaurantHelper {

    fun save(ownerEmail: String, restaurant: Restaurant, listener: OnCompleteListener<Void?>){
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