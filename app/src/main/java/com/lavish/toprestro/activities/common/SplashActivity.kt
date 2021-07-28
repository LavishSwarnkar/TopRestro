package com.lavish.toprestro.activities.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lavish.toprestro.App;
import com.lavish.toprestro.activities.user.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App app = ((App) getApplicationContext());

        if(app.isLoggedIn())
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));

        finish();
    }
}
