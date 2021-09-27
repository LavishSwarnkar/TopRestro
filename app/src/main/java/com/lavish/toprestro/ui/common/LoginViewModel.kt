package com.lavish.toprestro.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.data.RestaurantDao
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.common.ProfileCreator
import com.lavish.toprestro.firebaseHelpers.owner.OwnerRestaurantsFetcher
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.other.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@DelicateCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    var restaurantDao: RestaurantDao,
    var prefs: NewPrefs
) : ViewModel() {

    private var _loginHelperEvents = MutableStateFlow<UiEvents>(UiEvents.Empty)
    var uiEvents: StateFlow<UiEvents> = _loginHelperEvents

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
                            OwnerRestaurantsFetcher(restaurantDao)
                                    .fetch(emailId, object : OnCompleteListener<Void?>{
                                        override fun onResult(t: Void?) {
                                            this@LoginViewModel.listener.onResult(profile.name!!)
                                        }

                                        override fun onError(e: String) {
                                            listener.onError(it.toString())
                                        }
                                    })
                        }

                        //Done!
                        else {
                            this@LoginViewModel.listener.onResult(profile.name!!)
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
        viewModelScope.launch {
            prefs.saveProfile(profile, type)
        }
    }

    private fun inputName() {
        _loginHelperEvents.value = UiEvents.InputName()
    }

    fun createAccount(name: String) {
        val profile = Profile(name, emailId)

        val listener = object : OnCompleteListener<Void?> {
            override fun onResult(t: Void?) {
                saveProfile(profile, type)
                this@LoginViewModel.listener.onResult(name)
            }

            override fun onError(e: String) {
                this@LoginViewModel.listener.onError(e)
            }
        }

        ProfileCreator().createProfile(profile, type, listener)
    }

}

sealed class UiEvents {
    class InputName: UiEvents()
    object Empty: UiEvents()
}