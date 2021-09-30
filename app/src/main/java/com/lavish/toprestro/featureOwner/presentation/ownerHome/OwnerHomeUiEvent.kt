package com.lavish.toprestro.featureOwner.presentation.ownerHome

sealed class OwnerHomeUiEvent {

    object TaskSucceeded: OwnerHomeUiEvent()
    class TaskFailed(val e: String): OwnerHomeUiEvent()

}