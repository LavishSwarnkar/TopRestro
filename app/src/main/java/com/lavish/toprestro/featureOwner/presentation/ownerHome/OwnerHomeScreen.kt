package com.lavish.toprestro.featureOwner.presentation.ownerHome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.lavish.compose.ui.theme.TopRestroTheme
import com.lavish.toprestro.R
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import kotlinx.coroutines.flow.collect

lateinit var state: State<OwnerHomeState>

@Composable
fun OwnerHomeScreen(
    navController: NavController,
    viewModel: OwnerHomeViewModel = hiltViewModel()
) {

    state = viewModel.state

    val scaffoldState = rememberScaffoldState()

    TopRestroTheme {
        Scaffold(scaffoldState = scaffoldState) {
            Surface(
                color = MaterialTheme.colors.background
            ) {
                OwnerHome(viewModel)
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when(event) {
                is OwnerHomeUiEvent.TaskSucceeded ->
                    scaffoldState.snackbarHostState.showSnackbar("Done...")
                is OwnerHomeUiEvent.TaskFailed ->
                    scaffoldState.snackbarHostState.showSnackbar("Failed: ${event.e}")
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurant: Restaurant) {
    Text(
        text = restaurant.name,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .shadow(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.primary)
            .clickable { }
            .padding(16.dp)
    )
}

@Preview
@Composable
fun ReviewItem() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(color = MaterialTheme.colors.primary.copy(alpha = 0.1f))
        .padding(8.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Lavish Swarnkar",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "27 Sep (RJ 14)",
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            }

            RatingBar(
                value = 3f,
                ratingBarStyle = RatingBarStyle.HighLighted,
                onValueChange = {},
                activeColor = MaterialTheme.colors.primary,
                hideInactiveStars = true
            ) {}
        }

        Text(
            text = "ABCD",
            modifier = Modifier.padding(8.dp)
        )

        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.align(End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reply),
                contentDescription = "Reply"
            )
        }
    }
}

@Composable
fun OwnerHome(viewModel: OwnerHomeViewModel) {
    when (state.value.uiState) {
        is UiState.Loading -> Status("Loading...")
        is UiState.OfflineError -> OfflineStatus(viewModel)
        is UiState.Error -> Status("Error : ${(state.value.uiState as UiState.Error).e}...")
        is UiState.Loaded -> MainView(viewModel)
    }
}

@Composable
fun MainView(viewModel: OwnerHomeViewModel) {
    Column {
        Restaurants(viewModel)
        Reviews(viewModel)
    }
}

//------------------- Restaurants --------------------

@Composable
fun Restaurants(viewModel: OwnerHomeViewModel) {
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
fun Reviews(viewModel: OwnerHomeViewModel) {
    LazyColumn {
        items(state.value.reviews) { review ->
            Text(
                text = review.review,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.onEvent(
                            OwnerHomeEvent.ReplyReview(
                                review, "Okay!"
                            )
                        )
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
        style = MaterialTheme.typography.h6,
        maxLines = 10
    )
}

@Composable
fun OfflineStatus(viewModel: OwnerHomeViewModel) {
    Row {
        Status("You are offline")

        Button(onClick = {
            viewModel.onEvent(OwnerHomeEvent.Refresh)
        }) {
            Text("Retry")
        }
    }
}
