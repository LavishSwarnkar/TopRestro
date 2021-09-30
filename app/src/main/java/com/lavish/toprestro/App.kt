package com.lavish.toprestro

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.view.ViewGroup
import android.view.Window
import com.lavish.toprestro.old.models.Profile
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.other.Prefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    var updatedRestro: Restaurant? = null
    var profile: Profile? = null
    var loggedInAs: String? = null

    private var dialog: Dialog? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        val pair = Prefs(this).getProfile()

        if(pair != null){
            profile = pair.first
            loggedInAs = pair.second
        }
    }

    fun showLoadingDialog(context: Context?) {
        if (dialog != null && dialog!!.isShowing) return
        dialog = Dialog(context!!, R.style.LoaderStyle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_loader)
        dialog!!.setCancelable(false)
        if (dialog!!.window == null) return
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.show()
    }

    fun hideLoadingDialog() {
        if (dialog != null) dialog!!.dismiss()
    }

    val isLoggedIn: Boolean
        get() = profile != null

    val isOffline: Boolean
        get() {
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return !(wifiNetworkInfo!!.isConnected || dataNetworkInfo!!.isConnected)
        }
}