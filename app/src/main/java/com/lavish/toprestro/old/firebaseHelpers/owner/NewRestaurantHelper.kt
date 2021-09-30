package com.lavish.toprestro.old.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.models.Restaurant

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