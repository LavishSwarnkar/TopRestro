package com.lavish.toprestro.featureOwner.presentation.ownerHome

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.lavish.compose.ui.theme.TopRestroTheme
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OwnerHomeScreen: ComponentActivity() {

    private val viewModel: OwnerHomeViewModel by viewModels()
    private val state = viewModel.state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listenToUiEvents()

        setContent {
            TopRestroTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    OwnerHome()
                }
            }
        }
    }

    @Composable
    fun OwnerHome() {
        when (state.value.uiState) {
            is UiState.Loading -> Status("Loading...")
            is UiState.OfflineError -> OfflineStatus()
            is UiState.Error -> Status("Error : ${(state.value.uiState as UiState.Error).e}...")
            is UiState.Loaded -> MainView()
        }
    }

    @Composable
    fun MainView() {
        Column {
            Restaurants()
            Reviews()
        }
    }

    //------------------- Restaurants --------------------

    @Composable
    fun Restaurants() {
        Column {
            Text(
                state.value.restaurants.toString(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )

            Button(onClick = {
                viewModel.onEvent(OwnerHomeEvent.CreateNewRestaurant(
                    getFakeRestaurant()
                ))
            }) {
                Text("+ Restaurant")
            }
        }
    }

    private fun getFakeRestaurant(): Restaurant {
        val ts = "${System.currentTimeMillis()}"
        return Restaurant(ts, ts, ts, ts, 0f, 0)
    }

    //------------------- Reviews --------------------

    @Composable
    fun Reviews() {
        LazyColumn {
            items(state.value.reviews) { review ->
                Text(
                    text = review.review,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(8.dp)
                        .clickable {
                            viewModel.onEvent(OwnerHomeEvent.ReplyReview(
                                review, "Okay!"
                            ))
                        }
                )
            }
        }
    }

    //------------------- UI State Text --------------------

    @Composable
    fun Status(
        msg: String
    ) {
        Text(
            text = msg,
            style = MaterialTheme.typography.h6
        )
    }

    @Composable
    fun OfflineStatus() {
        Row {
            Status("You are offline")

            Button(onClick = {
                viewModel.onEvent(OwnerHomeEvent.Refresh)
            }) {
                Text("Retry")
            }
        }
    }

    //------------------- UI Events ------------------------

    private fun listenToUiEvents() {
        lifecycleScope.launchWhenResumed {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    is OwnerHomeUiEvent.TaskSucceeded -> showToast("Done...")
                    is OwnerHomeUiEvent.TaskFailed -> showToast("Failed: ${event.e}")
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}