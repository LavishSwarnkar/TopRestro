package com.lavish.toprestro.firebaseHelpers.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.models.Restaurant

class UsersFetcher {

    fun fetch(type: String, listener: OnCompleteListener<List<Profile>>){
        FirebaseFirestore.getInstance()
                .collection(type)
                .get()
                .addOnSuccessListener {
                    val profiles = mutableListOf<Profile>()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists())
                            profiles.add(doc.toObject(Profile::class.java)!!)
                    }

                    listener.onResult(profiles)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}