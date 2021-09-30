package com.lavish.toprestro.featureOwner.presentation.ownerHome

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.domain.usecases.OwnerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerHomeViewModel @Inject constructor(
    private val ownerUseCases: OwnerUseCases
): ViewModel() {

    //Offline error, logout()

    private val _state = mutableStateOf(OwnerHomeState.default)
    val state: State<OwnerHomeState> = _state

    private val _eventFlow = MutableSharedFlow<OwnerHomeUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        refresh()
    }

    private fun refresh() {
        getRestaurants()
        getReviews()
    }

    fun onEvent(event: OwnerHomeEvent) {
        when(event) {
            is OwnerHomeEvent.Refresh -> refresh()

            is OwnerHomeEvent.CreateNewRestaurant -> {
                createNewRestaurant(event.restaurant)
            }

            is OwnerHomeEvent.ReplyReview -> {
                replyReview(event.review, event.reply)
            }

            is OwnerHomeEvent.Logout -> {
                logout()
            }
        }
    }

    private fun getReviews() {
        viewModelScope.launch {
            try {
                val reviews = ownerUseCases.getReviews().toMutableList()

                _state.value = state.value.copy(
                    reviews = reviews,
                    uiState = UiState.Loaded
                )
            } catch (e: Exception) {
                _state.value = state.value.copy(
                    uiState = UiState.Error(e.message.toString())
                )
            }

        }
    }

    private fun getRestaurants() {
        ownerUseCases.getRestaurants()
            .onEach { restaurants ->
                _state.value = state.value.copy(
                    restaurants = restaurants
                )
            }
            .launchIn(viewModelScope)
    }

    private fun createNewRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                ownerUseCases.createNewRestaurant(restaurant)
                _eventFlow.emit(OwnerHomeUiEvent.TaskSucceeded)
            } catch (e: Exception) {
                _eventFlow.emit(OwnerHomeUiEvent.TaskFailed(e.message.toString()))
            }
        }
    }

    private fun replyReview(review: Review, reply: String) {
        viewModelScope.launch {
            try {
                ownerUseCases.replyReview(review, reply)

                //Remove review from the list
                val reviews = state.value.reviews
                reviews.remove(review)

                _state.value = state.value.copy(
                    reviews = reviews
                )

                _eventFlow.emit(OwnerHomeUiEvent.TaskSucceeded)
            } catch (e: Exception) {
                _eventFlow.emit(OwnerHomeUiEvent.TaskFailed(e.message.toString()))
            }
        }
    }

    private fun logout() {

    }

    /*private fun doTask(
        task: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                task()
                _eventFlow.emit(OwnerHomeUiEvent.TaskSucceeded)
            } catch (e: Exception) {
                _eventFlow.emit(OwnerHomeUiEvent.TaskFailed(e.message.toString()))
            }
        }
    }*/
}
