package com.lavish.toprestro.old.firebaseHelpers.common

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.other.ACCESS_DENIED

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
                            restaurant.rId = doc.id
                            restaurants.add(restaurant)
                        }
                    }

                    restaurants = restaurants.sortedWith { o1, o2 ->
                        if (o2.avgRating == o1.avgRating)
                            return@sortedWith o1.name!!.compareTo(o2.name!!)
                        o2.avgRating.compareTo(o1.avgRating)
                    }.toMutableList()

                    listener.onResult(restaurants)
                }
                .addOnFailureListener {
                    //Permission denied
                    if(it is FirebaseFirestoreException && it.code.value() == 7)
                        listener.onError(ACCESS_DENIED)
                    else
                        listener.onError(it.toString())
                }
    }

}