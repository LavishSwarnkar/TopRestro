package com.lavish.toprestro.ui.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.dialogs.OnInputCompleteListener
import com.lavish.toprestro.dialogs.TextInputDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.other.*
import com.lavish.toprestro.ui.admin.AdminActivity
import com.lavish.toprestro.ui.owner.OwnerActivity
import com.lavish.toprestro.ui.user.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    val loginViewModel: LoginViewModel by viewModels()

    private var userType: String? = null
    private var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        app = applicationContext as App
    }

    //Handle Button clicks ----------------------------------------------

    fun loginAsUser(view: View?) {
        login(TYPE_USER)
    }

    fun loginAsOwner(view: View?) {
        login(TYPE_OWNER)
    }

    fun loginAsAdmin(view: View?) {
        login(TYPE_ADMIN)
    }


    //Fire Login Intent ----------------------------------------------

    private val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()) { result: FirebaseAuthUIAuthenticationResult -> onSignInResult(result) }

    public fun login(userType: String) {
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
        observeLoginEvents()

        loginViewModel
                .postLogin(emailId!!, userType!!, object : OnCompleteListener<String> {
                    override fun onResult(name: String) {
                        app!!.hideLoadingDialog()

                        //Welcome
                        Toast.makeText(this@LoginActivity, String.format(getString(R.string.welcome), name), Toast.LENGTH_SHORT).show()

                        //Navigate to Activity
                        when(userType) {
                            TYPE_USER -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            TYPE_OWNER -> startActivity(Intent(this@LoginActivity, OwnerActivity::class.java))
                            TYPE_ADMIN -> startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                        }
                        finish()
                    }

                    override fun onError(e: String) {
                        app!!.hideLoadingDialog()

                        if(e == ACCESS_DENIED){
                            LogoutHelper().logout(this@LoginActivity, navigate = false)
                            ErrorDialog(this@LoginActivity).show(e)
                            return
                        }

                        ErrorDialog(this@LoginActivity).show(
                                getString(R.string.error_fetch_profile) + e
                        )
                    }
                })
    }

    private fun observeLoginEvents() {
        lifecycleScope.launch {
            loginViewModel.uiEvents.collect {
                when(it) {
                    is UiEvents.InputName -> {
                        val listener = object : OnInputCompleteListener {
                            override fun onSubmit(input: String) {
                                loginViewModel.createAccount(input)
                            }
                        }

                        TextInputDialog(this@LoginActivity)
                            .takeInput("Create new account", R.drawable.ic_account, "Name", EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS, "Enter", false, listener =  listener)
                    }
                }
            }
        }
    }
}