package com.lavish.toprestro.other

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.App
import com.lavish.toprestro.ui.common.LoginActivity

class LogoutHelper {

    fun logout(context: Context, navigate: Boolean = true){
        FirebaseAuth.getInstance().signOut()
        Prefs(context).clear()

        (context.applicationContext as App).profile = null
        (context.applicationContext as App).loggedInAs = null

        if(navigate){
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as AppCompatActivity).finish()
        }
    }
}