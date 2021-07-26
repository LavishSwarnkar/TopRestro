package com.lavish.toprestro.firebaseHelpers.common

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.models.Profile

class ProfileCreator {

    fun createProfile(profile: Profile, type: String, listener: OnCompleteListener<Void>) {
        FirebaseFirestore.getInstance()
                .document("${type}/${profile.emailId}")
                .set(profile)
                .addOnSuccessListener { listener.onResult(null) }
                .addOnFailureListener { listener.onError(it.message) }
    }

}