package com.lavish.toprestro.featureOwner.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.lavish.toprestro.featureOwner.domain.model.Profile
import com.lavish.toprestro.featureOwner.domain.repository.LoginStatus
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import kotlinx.coroutines.flow.first

class PrefsRepositoryImpl(
    private val datastore: DataStore<Preferences>
): PrefsRepository {

    override suspend fun saveProfile(profile: Profile, type: String) {
        datastore.edit {
            it[stringPreferencesKey(KEY_PROFILE)] = Gson().toJson(profile)
            it[stringPreferencesKey(KEY_LOGGED_IN_AS)] = type
        }
    }

    override suspend fun getProfile(): Profile {
        val profileStr = datastore.data.first()[stringPreferencesKey(KEY_PROFILE)]
        return Gson().fromJson(profileStr, Profile::class.java)
    }

    override suspend fun getLoginStatus(): LoginStatus {
        val data = datastore.data.first()

        val profileStr = data[stringPreferencesKey(KEY_PROFILE)]
        val profile = Gson().fromJson(profileStr, Profile::class.java)
        val loggedInAs = data[stringPreferencesKey(KEY_LOGGED_IN_AS)]

        if(profileStr == null || loggedInAs == null)
            return LoginStatus.NotLoggedIn

        return LoginStatus.LoggedIn(profile, loggedInAs)
    }

    override suspend fun logout() {
        FirebaseAuth.getInstance().signOut()
        datastore.edit {
            it.clear()
        }
    }

    companion object {
        private const val KEY_PROFILE = "profile"
        private const val KEY_LOGGED_IN_AS = "loggedInAs"
    }

}