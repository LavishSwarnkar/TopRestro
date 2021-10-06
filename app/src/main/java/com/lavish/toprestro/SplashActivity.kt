package com.lavish.toprestro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lavish.toprestro.featureLogin.presentation.LoginActivity
import com.lavish.toprestro.featureOwner.domain.repository.LoginStatus
import com.lavish.toprestro.featureOwner.domain.repository.PrefsRepository
import com.lavish.toprestro.featureOwner.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: PrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val loginStatus = prefs.getLoginStatus()

            when(loginStatus) {
                is LoginStatus.NotLoggedIn -> {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                is LoginStatus.LoggedIn -> {

                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))

                /*when(loginStatus.loggedInAs) {
                        TYPE_USER -> startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        TYPE_OWNER -> startActivity(Intent(this@SplashActivity, OwnerActivity::class.java))
                        TYPE_ADMIN -> startActivity(Intent(this@SplashActivity, AdminActivity::class.java))
                    }*/
                }
            }

            finish()
        }
    }
}