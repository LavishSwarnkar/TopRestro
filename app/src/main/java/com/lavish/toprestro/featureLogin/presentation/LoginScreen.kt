package com.lavish.toprestro.featureLogin.presentation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.firebase.ui.auth.AuthUI
import com.lavish.compose.ui.theme.PurpleFaded
import com.lavish.compose.ui.theme.PurpleFadedBorder
import com.lavish.toprestro.R
import com.lavish.toprestro.other.TYPE_ADMIN
import com.lavish.toprestro.other.TYPE_OWNER
import com.lavish.toprestro.other.TYPE_USER
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    signInLauncher: ActivityResultLauncher<Intent>,
    onUserTypeSelected: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(R.drawable.logo_login),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .padding(top = 50.dp)
                .wrapContentWidth()
                .wrapContentHeight()
        )

        //Colorful background
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 50.dp)
            .border(
                width = 2.dp,
                color = PurpleFadedBorder,
                shape = RoundedCornerShape(30.dp)
            )
            .background(
                color = PurpleFaded,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Heading
            Text(
                text = "Login as",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.primary
            )

            Spacer(Modifier.height(30.dp))

            UserButton(text = "User") {
                onUserTypeSelected(TYPE_USER)
                login(signInLauncher)
            }

            UserButton(
                text = "Owner",
                modifier = Modifier.padding(top = 8.dp)
            ) {
                onUserTypeSelected(TYPE_OWNER)
                login(signInLauncher)
            }

            UserButton(
                text = "Admin",
                modifier = Modifier.padding(top = 8.dp)
            ) {
                onUserTypeSelected(TYPE_ADMIN)
                login(signInLauncher)
            }
        }
    }
}

@Composable
fun UserButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Bold)
        )
    }
}

fun login(signInLauncher: ActivityResultLauncher<Intent>) {
    val providers = listOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    // Create and launch sign-in intent
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()
    signInLauncher.launch(signInIntent)
}