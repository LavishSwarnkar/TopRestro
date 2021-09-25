package com.lavish.toprestro.ui.user

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.common.AllRestaurantsFetcher
import com.lavish.toprestro.firebaseHelpers.user.ReviewsFetcher
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.ACCESS_DENIED
import com.lavish.toprestro.other.NetworkStatusHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val networkStatusHelper: NetworkStatusHelper
): ViewModel(), LifecycleObserver {

    private val _restroListUistate = MutableStateFlow<UiState>(UiState.Empty)
    val restroListUiState: StateFlow<UiState> = _restroListUistate

    private val _restroUiState = MutableStateFlow<UiState>(UiState.Empty)
    val restroUiState: StateFlow<UiState> = _restroUiState

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
                    _restroListUistate.value = UiState.ShowData(t)
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

}

sealed class UiState {
    object Empty: UiState()
    object Loading: UiState()
    object Offline: UiState()
    object AccessDenied: UiState()
    class ShowData<T>(val data: T): UiState()
    class Error(val e: String): UiState()
}