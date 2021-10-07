package com.lavish.toprestro.featureUser.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureUser.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val userUseCases: UserUseCases
): ViewModel() {

    val restaurants = MutableLiveData<List<Restaurant>>(emptyList())

    fun getRestaurants() {
        viewModelScope.launch {
            restaurants.value = userUseCases.getRestaurants()
        }
    }
    
}