package com.lavish.toprestro.firebaseHelpers.common

import android.content.Context
import android.view.inputmethod.EditorInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.dialogs.OnInputCompleteListener
import com.lavish.toprestro.dialogs.TextInputDialog
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.other.Prefs

class LoginHelper(val context: Context) {

    lateinit var emailId: String
    lateinit var type: String
    lateinit var listener: OnCompleteListener<String>

    fun postLogin(emailId: String, type: String, listener: OnCompleteListener<String>){
        this.emailId = emailId
        this.type = type
        this.listener = listener

        FirebaseFirestore.getInstance()
                .document("${type}/${emailId}")
                .get()
                .addOnSuccessListener {
                    //Existing user
                    if(it.exists()){
                        val profile = it.toObject(Profile::class.java)
                        saveProfile(profile)

                        this@LoginHelper.listener.onResult(profile?.name)
                    }

                    //New user
                    else {
                        inputName()
                    }
                }
    }

    private fun saveProfile(profile: Profile?) {
        Prefs(context).saveProfile(profile)
        (context.applicationContext as App).profile = profile
    }

    private fun inputName() {
        val listener = object : OnInputCompleteListener {
            override fun onSubmit(input: String) {
                createAccount(input)
            }
        }

        TextInputDialog(context)
                .takeInput("Create new account", R.drawable.ic_account, "Name", EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS, "Enter", false, listener)
    }

    private fun createAccount(name: String) {
        val profile = Profile(name, emailId)

        val listener = object : OnCompleteListener<Void>{
            override fun onResult(t: Void?) {
                saveProfile(profile)
                this@LoginHelper.listener.onResult(name)
            }

            override fun onError(e: String?) {
                this@LoginHelper.listener.onError(e)
            }
        }

        ProfileCreator().createProfile(profile, type, listener)
    }

}