package com.lavish.toprestro.featureOwner.domain.repository

import com.lavish.toprestro.featureOwner.domain.model.Profile

//Local preferences repository to access owner profile
interface PrefsRepository {

    suspend fun saveProfile(profile: Profile, type: String)

    suspend fun getProfile(): Profile

    suspend fun getLoginStatus(): LoginStatus

    suspend fun logout()
}

sealed class LoginStatus {
    class LoggedIn(val profile: Profile, val loggedInAs: String) : LoginStatus()
    object NotLoggedIn: LoginStatus()
}