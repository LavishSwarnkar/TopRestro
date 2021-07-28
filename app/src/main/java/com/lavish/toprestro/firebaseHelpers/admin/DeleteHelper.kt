package com.lavish.toprestro.firebaseHelpers.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.other.Constants.TYPE_OWNER

class DeleteHelper {

    fun delete(id: String, type: String, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
                .document("$type/$id")
                .delete()
                .addOnSuccessListener {
                    //if owner, delete all its restaurants
                    if(type == TYPE_OWNER)
                        deleteRestaurants(id, listener)

                    //Done!
                    else
                        listener.onResult(null)
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

    private fun deleteRestaurants(ownerEmail: String, listener: OnCompleteListener<Void?>) {
        FirebaseFirestore.getInstance()
                .collection("restaurants")
                .whereEqualTo("ownerEmail", ownerEmail)
                .get()
                .addOnSuccessListener {
                    val batch = FirebaseFirestore.getInstance().batch()

                    it.documents.forEach { doc ->
                        if(doc != null && doc.exists())
                            batch.delete(doc.reference)
                    }

                    batch.commit().addOnSuccessListener {
                        listener.onResult(null)
                    }.addOnFailureListener { e ->
                        listener.onError(e.toString())
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

}