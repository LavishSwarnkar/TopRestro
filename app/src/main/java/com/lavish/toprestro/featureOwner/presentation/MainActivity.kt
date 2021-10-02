package com.lavish.toprestro.featureOwner.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lavish.compose.ui.theme.TopRestroTheme
import com.lavish.toprestro.featureOwner.presentation.ownerHome.OwnerHomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TopRestroTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "owner"
                    ) {

                        //Screen 1 : NotesScreen
                        composable(route = "owner") {
                            OwnerHomeScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}