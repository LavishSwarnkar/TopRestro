package com.lavish.toprestro.old.ui.user

import androidx.lifecycle.*
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.firebaseHelpers.common.AllRestaurantsFetcher
import com.lavish.toprestro.old.firebaseHelpers.user.ReviewsFetcher
import com.lavish.toprestro.old.models.Profile
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.models.Review
import com.lavish.toprestro.old.other.ACCESS_DENIED
import com.lavish.toprestro.old.other.LoginStatus
import com.lavish.toprestro.old.other.NetworkStatusHelper
import com.lavish.toprestro.old.other.NewPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val prefs: NewPrefs,
    val networkStatusHelper: NetworkStatusHelper
): ViewModel(), LifecycleObserver {

    private val _restroListUistate = MutableStateFlow<UiState>(UiState.Empty)
    val restroListUiState: StateFlow<UiState> = _restroListUistate

    private val _restroUiState = MutableStateFlow<UiState>(UiState.Empty)
    val restroUiState: StateFlow<UiState> = _restroUiState

    private val _userProfile: MutableLiveData<Profile> = MutableLiveData()
    val userProfile: LiveData<Profile> = _userProfile

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            val status = prefs.getLoginStatus()
            when(status) {
                is LoginStatus.LoggedIn -> {
                    _userProfile.value = status.profile
                }
            }
        }
    }

    //For RestroListFragment
    fun fetchRestros() {
        //If User is Offline
        if(networkStatusHelper.isOffline()){
            _restroListUistate.value = UiState.Offline
            return
        }

        _restroListUistate.value = UiState.Loading

        //Fetch data
        AllRestaurantsFetcher()
            .fetch(object : OnCompleteListener<MutableList<Restaurant>> {
                override fun onResult(t: MutableList<Restaurant>) {
                    val livedata = MutableLiveData(t)
                    _restroListUistate.value = UiState.ShowData(livedata)
                }

                override fun onError(e: String) {
                    if(e == ACCESS_DENIED) {
                        _restroListUistate.value = UiState.AccessDenied
                    } else {
                        _restroListUistate.value = UiState.Error(e)
                    }
                }
            })
    }

    fun fetchReviews(restroId: String) {
        //If User is Offline
        if(networkStatusHelper.isOffline()){
            _restroUiState.value = UiState.Offline
            return
        }

        _restroUiState.value = UiState.Loading

        ReviewsFetcher()
            .fetch(restroId, object : OnCompleteListener<MutableList<Review>>{
                override fun onResult(t: MutableList<Review>) {
                    _restroUiState.value = UiState.ShowData(t)
                }

                override fun onError(e: String) {
                    _restroUiState.value = UiState.Error(e)
                }
            })
    }

    fun onRestaurantRatingChanged(changedRestro: Restaurant) {
        if(restroListUiState.value is UiState.ShowData<*>) {
            val livedata = (restroListUiState.value as UiState.ShowData<*>).data
                    as MutableLiveData<MutableList<Restaurant>>

            for(restro in livedata.value!!){
                if(changedRestro.id == restro.id){
                    livedata.value!!.remove(restro)
                    livedata.value!!.add(changedRestro)
                    livedata.postValue(livedata.value)
                    break
                }
            }
        }
    }

}

sealed class UiState {
    object Empty: UiState()
    object Loading: UiState()
    object Offline: UiState()
    object AccessDenied: UiState()
    class ShowData<T>(val data: T): UiState()
    class Error(val e: String): UiState()
}