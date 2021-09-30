package com.lavish.toprestro.old.other

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.lavish.toprestro.App
import com.lavish.toprestro.old.models.Profile
import com.lavish.toprestro.old.models.Restaurant

class Prefs(val context: Context) {

    fun getProfile(): Pair<Profile, String>? {
        val prefs = context.getSharedPreferences("main", MODE_PRIVATE)
        val profileStr = prefs.getString("profile", null) ?: return null
        val loggedInAs = prefs.getString("loggedInAs", null) ?: return null

        return Pair(Gson().fromJson(profileStr, Profile::class.java), loggedInAs)
    }

    fun saveProfile(profile: Profile?, type: String){
        context.getSharedPreferences("main", MODE_PRIVATE)
                .edit()
                .putString("profile", Gson().toJson(profile))
                .putString("loggedInAs", type)
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

    fun saveNewRestro(restro: Restaurant) {
        val restros = mutableListOf<Restaurant>()
        restros.addAll(getRestros())
        restros.add(restro)

        saveRestros(restros)
    }

    fun clear() {
        (context.applicationContext as App).profile = null

        context.getSharedPreferences("main", MODE_PRIVATE)
                .edit()
                .clear()
                .commit()
    }

}