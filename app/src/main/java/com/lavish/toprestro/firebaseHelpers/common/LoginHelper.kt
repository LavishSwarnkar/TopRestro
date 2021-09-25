package com.lavish.toprestro.firebaseHelpers.common

import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.dialogs.OnInputCompleteListener
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.other.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class LoginHelper() {

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
                        val profile = it.toObject(Profile::class.java)!!
                        saveProfile(profile, type)

                        //Fetch & save Restros for owner
                        if(type == TYPE_OWNER){
                            /*OwnerRestaurantsFetcher(context)
                                    .fetch(emailId, object : OnCompleteListener<Void?>{
                                        override fun onResult(t: Void?) {
                                            this@LoginHelper.listener.onResult(profile.name!!)
                                        }

                                        override fun onError(e: String) {
                                            listener.onError(it.toString())
                                        }
                                    })*/
                        }

                        //Done!
                        else {
                            this@LoginHelper.listener.onResult(profile.name!!)
                        }
                    }

                    //New user
                    else {
                        if(type == TYPE_ADMIN){
                            listener.onError(ACCESS_DENIED)
                            return@addOnSuccessListener
                        }

                        inputName()
                    }
                }
                .addOnFailureListener {
                    listener.onError(it.toString())
                }
    }

    private fun saveProfile(profile: Profile, type: String) {
        GlobalScope.launch {
            //NewPrefs(context).saveProfile(profile, type)
        }
    }

    private fun inputName() {
        val listener = object : OnInputCompleteListener {
            override fun onSubmit(input: String) {
                createAccount(input)
            }
        }

        /*TextInputDialog(context)
                .takeInput("Create new account", R.drawable.ic_account, "Name", EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS, "Enter", false, listener =  listener)*/
    }

    private fun createAccount(name: String) {
        val profile = Profile(name, emailId)

        val listener = object : OnCompleteListener<Void?> {
            override fun onResult(t: Void?) {
                saveProfile(profile, type)
                this@LoginHelper.listener.onResult(name)
            }

            override fun onError(e: String) {
                this@LoginHelper.listener.onError(e)
            }
        }

        ProfileCreator().createProfile(profile, type, listener)
    }

}