package com.lavish.toprestro.other

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED

class NetworkStatusHelper(val context: Context) {

    fun isOffline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val currentNetwork = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(currentNetwork)

        caps?.let {
            return !it.hasCapability(NET_CAPABILITY_VALIDATED)
        }
        return true
    }

}