package com.lavish.toprestro.firebaseHelpers.common

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant

class AllRestaurantsFetcher() {

    fun fetch(listener: OnCompleteListener<List<Restaurant>>){
        FirebaseFirestore.getInstance()
                .collection("restaurants")
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

                    listener.onResult(restaurants)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}