package com.lavish.toprestro.firebaseHelpers.owner

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener

class RestaurantImageUploader {

    fun uploadImage(imageUri: Uri, ownerEmail: String, restroName: String, listener: OnCompleteListener<String>) {
        val storage = FirebaseStorage.getInstance().reference
        val ref = storage.child("$ownerEmail/$restroName.jpg")

        ref.putFile(imageUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                ref.downloadUrl.addOnSuccessListener {
                    listener.onResult(it.toString())
                }.addOnFailureListener {
                    listener.onError(it.toString())
                }
            } else {
                listener.onError(task.exception.toString())
            }
        }
    }

}