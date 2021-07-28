package com.lavish.toprestro.activities.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lavish.toprestro.App
import com.lavish.toprestro.activities.owner.OwnerActivity
import com.lavish.toprestro.activities.user.MainActivity
import com.lavish.toprestro.other.Constants.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = applicationContext as App

        if (app.isLoggedIn) {
            when(app.loggedInAs) {
                TYPE_USER -> startActivity(Intent(this, MainActivity::class.java))
                TYPE_OWNER -> startActivity(Intent(this, OwnerActivity::class.java))
                TYPE_ADMIN -> startActivity(Intent(this, MainActivity::class.java))
            }
        }

        else startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}