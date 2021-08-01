package com.lavish.toprestro.ui.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.ui.owner.OwnerActivity
import com.lavish.toprestro.ui.user.MainActivity
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.common.LoginHelper
import com.lavish.toprestro.other.Constants
import com.lavish.toprestro.other.Constants.*

class LoginActivity : AppCompatActivity() {
    private var userType: String? = null
    private var app: App? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = applicationContext as App
    }

    //Handle Button clicks ----------------------------------------------

    fun loginAsUser(view: View?) {
        login(Constants.TYPE_USER)
    }

    fun loginAsOwner(view: View?) {
        login(Constants.TYPE_OWNER)
    }

    fun loginAsAdmin(view: View?) {
        login(Constants.TYPE_ADMIN)
    }


    //Fire Login Intent ----------------------------------------------

    private val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()) { result: FirebaseAuthUIAuthenticationResult -> onSignInResult(result) }

    private fun login(userType: String) {
        this.userType = userType
        val providers = listOf(
                GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
        signInLauncher.launch(signInIntent)
    }

    //After login ---------------------------------------------------------

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            app!!.showLoadingDialog(this)
            postLogin(user!!.email)
        } else {
            Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show()
            //ErrorDialog(this).show(String.format(getString(R.string.error_login), result.resultCode))
        }
    }

    private fun postLogin(emailId: String?) {
        LoginHelper(this)
                .postLogin(emailId!!, userType!!, object : OnCompleteListener<String> {
                    override fun onResult(name: String) {
                        app!!.hideLoadingDialog()

                        //Welcome
                        Toast.makeText(this@LoginActivity, String.format(getString(R.string.welcome), name), Toast.LENGTH_SHORT).show()

                        //Navigate to Activity
                        when(userType) {
                            TYPE_USER -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            TYPE_OWNER -> startActivity(Intent(this@LoginActivity, OwnerActivity::class.java))
                            TYPE_ADMIN -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        finish()
                    }

                    override fun onError(e: String) {
                        app!!.hideLoadingDialog()
                        ErrorDialog(this@LoginActivity).show(
                                getString(R.string.error_fetch_profile) + e
                        )
                    }
                })
    }
}