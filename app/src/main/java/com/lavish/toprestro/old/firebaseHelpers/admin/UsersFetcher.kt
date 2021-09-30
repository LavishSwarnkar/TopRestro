package com.lavish.toprestro.old.firebaseHelpers.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.models.Profile

class UsersFetcher {

    fun fetch(type: String, listener: OnCompleteListener<List<Profile>>){
        FirebaseFirestore.getInstance()
                .collection(type)
                .get()
                .addOnSuccessListener { it ->
                    val profiles = mutableListOf<Profile>()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists())
                            profiles.add(doc.toObject(Profile::class.java)!!)
                    }

                    profiles.sortWith { o1, o2 -> o1!!.name!!.compareTo(o2!!.name!!) }

                    listener.onResult(profiles)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}