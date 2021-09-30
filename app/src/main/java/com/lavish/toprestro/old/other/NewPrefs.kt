package com.lavish.toprestro.old.other

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.lavish.toprestro.old.models.Profile
import kotlinx.coroutines.flow.first

class NewPrefs (val context: Context) {

    //Datastore object
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_FILE)

    //Save the profile
    suspend fun saveProfile(profile: Profile, type: String){
        context.dataStore.edit {
            it[stringPreferencesKey(PREFS_KEY_PROFILE)] = Gson().toJson(profile)
            it[stringPreferencesKey(PREFS_KEY_LOGGED_IN_AS)] = type
        }
    }

    //Returns the LoginStatus (& Profile, loggedInAs if LoggedIn)
    suspend fun getLoginStatus(): LoginStatus {
        val data = context.dataStore.data.first()

        val profileStr = data[stringPreferencesKey(PREFS_KEY_PROFILE)]
        val profile = Gson().fromJson(profileStr, Profile::class.java)
        val loggedInAs = data[stringPreferencesKey(PREFS_KEY_LOGGED_IN_AS)]

        if(profileStr == null || loggedInAs == null)
            return LoginStatus.NotLoggedIn

        return LoginStatus.LoggedIn(profile, loggedInAs)
    }

    suspend fun clear() {
        context.dataStore.edit {
            clear()
        }
    }
}

sealed class LoginStatus {
    class LoggedIn(val profile: Profile, val loggedInAs: String) : LoginStatus()
    object NotLoggedIn: LoginStatus()
}