package com.lavish.toprestro.firebaseHelpers.common

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Constants

class AllRestaurantsFetcher() {

    fun fetch(listener: OnCompleteListener<MutableList<Restaurant>>){
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .get()
                .addOnSuccessListener {
                    var restaurants = mutableListOf<Restaurant>()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists()){
                            val restaurant = doc.toObject(Restaurant::class.java)!!
                            restaurant.id = doc.id
                            restaurants.add(restaurant)
                        }
                    }

                    restaurants = restaurants.sortedWith {
                        o1, o2 -> o2!!.avgRating.compareTo(o1!!.avgRating)
                    }.toMutableList()

                    listener.onResult(restaurants)
                }
                .addOnFailureListener {
                    //Permission denied
                    if(it is FirebaseFirestoreException && it.code.value() == 7)
                        listener.onError(Constants.ACCESS_DENIED)
                    else
                        listener.onError(it.toString())
                }
    }

}