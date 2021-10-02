package com.lavish.toprestro.other

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.featureOwner.domain.model.Profile

class AdminHelper {

    fun checkIfAdmin(email: String, listener: OnCompleteListener<Profile?>) {
        FirebaseFirestore.getInstance()
            .document("admins/$email")
            .get()
            .addOnSuccessListener {
                if(it.exists())
                    listener.onResult(it.toObject(Profile::class.java)!!)
                else
                    listener.onResult(null)
            }
            .addOnFailureListener {
                listener.onError(it.toString())
            }
    }

    fun createAdmin(profile: Profile, listener: OnCompleteListener<Void?>){
        FirebaseFirestore.getInstance()
            .document("admins/${profile.emailId}")
            .set(profile)
            .addOnSuccessListener {
                listener.onResult(null)
            }
            .addOnFailureListener {
                listener.onError(it.toString())
            }
    }

}