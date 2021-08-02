package com.lavish.toprestro.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lavish.toprestro.App
import com.lavish.toprestro.ui.owner.OwnerActivity
import com.lavish.toprestro.ui.user.MainActivity
import com.lavish.toprestro.other.Constants.*
import com.lavish.toprestro.ui.admin.AdminActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = applicationContext as App

        if (app.isLoggedIn) {
            when(app.loggedInAs) {
                TYPE_USER -> startActivity(Intent(this, MainActivity::class.java))
                TYPE_OWNER -> startActivity(Intent(this, OwnerActivity::class.java))
                TYPE_ADMIN -> startActivity(Intent(this, AdminActivity::class.java))
            }
        }

        else startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }
}