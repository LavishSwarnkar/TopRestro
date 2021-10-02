package com.lavish.toprestro.featureOwner.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.lavish.toprestro.featureOwner.domain.model.Profile
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.domain.repository.RestaurantRepository
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
    val restaurantRepository: RestaurantRepository,
    val prefsRepository: PrefsRepository
) : ViewModel() {

    private var _loginHelperEvents = MutableStateFlow<UiEvents>(UiEvents.Empty)
    var uiEvents: StateFlow<UiEvents> = _loginHelperEvents

    lateinit var emailId: String
    lateinit var type: String
    lateinit var listener: OnCompleteListener<String>

    fun postLogin(emailId: String, type: String, listener: com.lavish.toprestro.other.OnCompleteListener<String>){
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
                            FirebaseFirestore.getInstance()
                                .collection("restaurants")
                                .whereEqualTo("ownerEmail", emailId)
                                .get()
                                .addOnSuccessListener {
                                    var restaurants = mutableListOf<Restaurant>()

                                    it.documents.forEach { doc ->
                                        if(doc != null && doc.exists()){
                                            doc.data?.set("id", doc.id)
                                            val restaurant = doc.toObject(Restaurant::class.java)!!
                                            restaurant.id = doc.id
                                            restaurants.add(restaurant)
                                        }
                                    }

                                    restaurants = restaurants.sortedWith(
                                        compareBy { it.name }
                                    ).toMutableList()

                                    //save Restaurants locally
                                    viewModelScope.launch {
                                        restaurantRepository.insertAllRestaurants(restaurants)
                                    }

                                    this@LoginViewModel.listener.onResult(profile.name!!)
                                }
                                .addOnFailureListener {
                                    listener.onError(it.toString())
                                }
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
            prefsRepository.saveProfile(profile, type)
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

        FirebaseFirestore.getInstance()
            .document("${type}/${profile.emailId}")
            .set(profile)
            .addOnSuccessListener { listener.onResult(null) }
            .addOnFailureListener { listener.onError(it.message.toString()) }
    }

}

sealed class UiEvents {
    class InputName: UiEvents()
    object Empty: UiEvents()
}