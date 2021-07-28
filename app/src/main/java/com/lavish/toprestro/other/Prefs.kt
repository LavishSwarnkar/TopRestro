package com.lavish.toprestro.other

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.models.Restaurant

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

    @SuppressLint("ApplySharedPref")
    fun saveRestros(restaurants: MutableList<Restaurant>) {
        context.getSharedPreferences("main", MODE_PRIVATE)
                .edit()
                .putString("restaurants", Gson().toJson(restaurants))
                .commit()
    }

    fun getRestros(): List<Restaurant> {
        val str = context.getSharedPreferences("main", MODE_PRIVATE)
                .getString("restaurants", null) ?: return emptyList()
        return Gson().fromJson(str, object : TypeToken<ArrayList<Restaurant>>() {}.type)
    }

}