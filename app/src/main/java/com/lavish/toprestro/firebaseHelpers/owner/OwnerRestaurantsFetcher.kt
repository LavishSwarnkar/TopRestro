package com.lavish.toprestro.firebaseHelpers.owner

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Prefs

class OwnerRestaurantsFetcher(val context: Context) {

    fun fetch(ownerEmail: String, listener: OnCompleteListener<Void>){
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerEmail)
                .get()
                .addOnSuccessListener {
                    val restaurants = mutableListOf<Restaurant>()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists()){
                            val restaurant = doc.toObject(Restaurant::class.java)!!
                            restaurant.id = doc.id
                            restaurants.add(restaurant)
                        }
                    }

                    //save Restaurants locally
                    Prefs(context).saveRestros(restaurants)

                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}