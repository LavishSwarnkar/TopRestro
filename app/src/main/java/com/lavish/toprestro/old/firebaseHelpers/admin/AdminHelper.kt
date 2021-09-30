package com.lavish.toprestro.old.firebaseHelpers.admin

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.models.Profile

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