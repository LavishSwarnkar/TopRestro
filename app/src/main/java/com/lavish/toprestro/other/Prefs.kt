package com.lavish.toprestro.other

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.google.gson.Gson
import com.lavish.toprestro.models.Profile

class Prefs(val context: Context) {

    fun getProfile(): Profile? {
        val profileStr = context.getSharedPreferences("main", MODE_PRIVATE)
                .getString("profile", null) ?: return null
        return Gson().fromJson(profileStr, Profile::class.java)
    }

    fun saveProfile(profile: Profile?){
        context.getSharedPreferences("main", MODE_PRIVATE)
                .edit()
                .putString("profile", Gson().toJson(profile))
                .apply()

    }

}