package com.lavish.toprestro.ui.common

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lavish.toprestro.App
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.AdminHelper
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.ui.owner.OwnerActivity
import com.lavish.toprestro.ui.user.MainActivity
import com.lavish.toprestro.other.Constants.*
import com.lavish.toprestro.ui.admin.AdminActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*AdminHelper()
                .createAdmin(Profile("Lavish Swarnkar", "lavishswarnkar@gmail.com"), object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        Toast.makeText(this@SplashActivity, "Done!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: String) {
                        Toast.makeText(this@SplashActivity, e, Toast.LENGTH_SHORT).show()
                    }
                })*/

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