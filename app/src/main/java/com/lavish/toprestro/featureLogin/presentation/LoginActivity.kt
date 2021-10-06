package com.lavish.toprestro.featureLogin.presentation

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.lavish.compose.ui.theme.TopRestroTheme
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.featureOwner.presentation.MainActivity
import com.lavish.toprestro.featureOwner.presentation.dialogs.ErrorDialog
import com.lavish.toprestro.featureOwner.presentation.dialogs.TextInputDialog
import com.lavish.toprestro.other.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    private var userType: String? = null
    private var app: App? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TopRestroTheme {
                Scaffold(
                    scaffoldState = rememberScaffoldState(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = Color.White,
                            elevation = 12.dp,
                            actions = {
                                IconButton(onClick = {
                                    //TODO: Logout
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.ic_logout),
                                        contentDescription = "Logout"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    Surface(
                        color = MaterialTheme.colors.background
                    ) {
                        LoginScreen(loginViewModel = loginViewModel, signInLauncher) { userType ->
                            this.userType = userType
                        }
                    }
                }
            }
        }

        app = applicationContext as App
    }

    //Login Intent ----------------------------------------------

    private val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
    ) {
            result: FirebaseAuthUIAuthenticationResult -> onSignInResult(result)
    }

    //After login ---------------------------------------------------------

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            app!!.showLoadingDialog(this)
            postLogin(user!!.email)
        } else {
            Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postLogin(emailId: String?) {
        observeLoginEvents()

        loginViewModel.postLogin(emailId!!, userType!!)
    }

    private fun observeLoginEvents() {
        lifecycleScope.launch {
            loginViewModel.loginEvent.collect {
                when(it) {
                    is LoginEvent.WelcomeAndNavigateUser -> {
                        app?.hideLoadingDialog()
                        welcomeAndNavigateUser(it.name)
                    }

                    is LoginEvent.InputName -> {
                        app?.hideLoadingDialog()
                        inputName()
                    }

                    is LoginEvent.AdminAccessDenied -> {
                        app?.hideLoadingDialog()
                        ErrorDialog(this@LoginActivity).show(ACCESS_DENIED)
                    }

                    is LoginEvent.Error -> {
                        app?.hideLoadingDialog()
                        ErrorDialog(this@LoginActivity).show(it.e)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun inputName() {
        TextInputDialog(this@LoginActivity)
            .takeInput(
                "Create new account",
                R.drawable.ic_account,
                "Name",
                EditorInfo.TYPE_TEXT_FLAG_CAP_WORDS,
                "Enter",
                false,
            ) { input ->
                loginViewModel.createAccount(input)
            }
    }

    private fun welcomeAndNavigateUser(name: String) {
        app?.hideLoadingDialog()

        //Welcome
        Toast.makeText(this@LoginActivity, String.format(getString(R.string.welcome), name), Toast.LENGTH_SHORT).show()

        //Navigate to Activity
        when(userType) {
            TYPE_USER -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            TYPE_OWNER -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            TYPE_ADMIN -> startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }
}