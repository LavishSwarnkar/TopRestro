package com.lavish.toprestro.firebaseHelpers.owner

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.data.RestaurantDao
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Restaurant
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class OwnerRestaurantsFetcher(val dao: RestaurantDao) {

    fun fetch(ownerEmail: String, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerEmail)
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

                    restaurants = restaurants.sortedWith(
                        compareBy { it.name }
                    ).toMutableList()

                    //save Restaurants locally
                    GlobalScope.launch {
                        dao.insertAllRestaurants(restaurants)
                    }

                    listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}