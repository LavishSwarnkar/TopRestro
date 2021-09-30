package com.lavish.toprestro.featureOwner.presentation.ownerHome

import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review

data class OwnerHomeState(
    var restaurants: List<Restaurant>,
    var reviews: MutableList<Review>,
    var uiState: UiState
) {
    companion object {
        val default = OwnerHomeState(
            emptyList(), mutableListOf(), UiState.Loading
        )
    }
}

sealed class UiState {
    object Loading: UiState()
    object Loaded: UiState()
    object OfflineError: UiState()
    class Error(val e: String): UiState()
}