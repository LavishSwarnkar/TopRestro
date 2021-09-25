package com.lavish.toprestro.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lavish.toprestro.other.*
import com.lavish.toprestro.other.LoginStatus.*
import com.lavish.toprestro.ui.admin.AdminActivity
import com.lavish.toprestro.ui.owner.OwnerActivity
import com.lavish.toprestro.ui.user.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: NewPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val loginStatus = prefs.getLoginStatus()

            when(loginStatus) {
                is NotLoggedIn -> {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                is LoggedIn -> {
                    val loggedInAs = (loginStatus as LoggedIn).loggedInAs

                    when(loggedInAs) {
                        TYPE_USER -> startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        TYPE_OWNER -> startActivity(Intent(this@SplashActivity, OwnerActivity::class.java))
                        TYPE_ADMIN -> startActivity(Intent(this@SplashActivity, AdminActivity::class.java))
                    }
                }
            }

            finish()
        }
    }
}