package com.lavish.toprestro.activities.common;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lavish.toprestro.App;
import com.lavish.toprestro.R;
import com.lavish.toprestro.activities.user.MainActivity;
import com.lavish.toprestro.dialogs.ErrorDialog;
import com.lavish.toprestro.firebaseHelpers.common.LoginHelper;
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener;
import com.lavish.toprestro.other.Constants;

import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private String userType;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = ((App) getApplicationContext());
    }

    //Handle Button clicks ----------------------------------------------

    public void loginAsUser(View view) {
        login(Constants.TYPE_USER);
    }

    public void loginAsOwner(View view) {
        login(Constants.TYPE_OWNER);
    }

    public void loginAsAdmin(View view) {
        login(Constants.TYPE_ADMIN);
    }



    //Fire Login Intent ----------------------------------------------

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void login(String userType) {
        this.userType = userType;

        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }


    //After login ---------------------------------------------------------

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {

        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            app.showLoadingDialog(this);
            postLogin(user.getEmail());
        } else {
            new ErrorDialog(this).show(
                    String.format(getString(R.string.error_login)
                            , result.getIdpResponse().getError().getErrorCode())
            );
        }
    }

    private void postLogin(String emailId) {
        new LoginHelper(this)
                .postLogin(emailId
                        , userType
                        , new OnCompleteListener<String>() {
                            @Override
                            public void onResult(String name) {

                                app.hideLoadingDialog();

                                //Welcome
                                Toast.makeText(LoginActivity.this
                                        , String.format(getString(R.string.welcome), name)
                                        , Toast.LENGTH_SHORT).show();

                                //Navigate to MainActivity
                                startActivity(
                                        new Intent(LoginActivity.this, MainActivity.class)
                                );
                                finish();
                            }

                            @Override
                            public void onError(String e) {

                                app.hideLoadingDialog();

                                new ErrorDialog(LoginActivity.this).show(
                                        getString(R.string.error_fetch_profile) + e
                                );
                            }
                        });
    }
}