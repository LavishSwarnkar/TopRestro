package com.lavish.toprestro.featureOwner.presentation.ownerHome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.lavish.compose.ui.theme.*
import com.lavish.toprestro.R
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import kotlinx.coroutines.flow.collect
import java.util.*

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
fun AppDialog(
    viewModel: OwnerHomeViewModel,
    modifier: Modifier = Modifier,
    dialogState: MutableState<Boolean>
) {

    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = { dialogState.value = false },
            title = null,
            text = null,
            buttons = {
                DialogLayout(viewModel, dialogState)
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun DialogLayout(viewModel: OwnerHomeViewModel, dialogState: MutableState<Boolean>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New restaurant"
            )

            Title(
                text = "New restaurant",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        val input = rememberSaveable { mutableStateOf("") }
        val error = remember { mutableStateOf(false) }
        OutlinedTextField(
            modifier = Modifier.padding(top = 8.dp),
            value = input.value,
            onValueChange = {
                input.value = it
                error.value = it.isBlank()
            },
            label = { Text("Name") },
            isError = error.value
        )

        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .align(End),
            onClick = {
                if(!input.value.isBlank()) {
                    viewModel.onEvent(OwnerHomeEvent.CreateNewRestaurant(
                        Restaurant(
                            id = "",
                            name = input.value,
                            imageURL = getRandomRestroImageUrl(),
                            ownerEmail = "", //Will be added in viewmodel
                            avgRating = 0f,
                            noOfRatings = 0
                        )
                    ))

                    dialogState.value = false
                }
            }
        ) {
            Text(text = "CREATE")
        }
    }
}

fun getRandomRestroImageUrl() = "https://picsum.photos/720/512?id=${Random().nextInt(100)}"

@Composable
fun RestaurantItem(restaurant: Restaurant) {
    Text(
        text = restaurant.name,
        style = MaterialTheme.typography.body1,
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

@Composable
fun ReviewItem(review: Review) {
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
                    text = review.userName,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = "${review.formattedDate()} (${review.restroName})",
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            }

            RatingBar(
                value = review.starRating,
                ratingBarStyle = RatingBarStyle.HighLighted,
                onValueChange = {},
                activeColor = getColorForRating(review.starRating),
                hideInactiveStars = true
            ) {}
        }

        Text(
            text = review.review,
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

fun getColorForRating(starRating: Float): Color {
    return when {
        starRating > 4 -> Stars5
        starRating > 3 -> Stars4
        starRating > 2 -> Stars3
        else -> Stars2
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
    val scrollState = rememberScrollState()

    Column(Modifier.scrollable(scrollState, Orientation.Vertical)) {
        Restaurants(viewModel)
        Divider()
        Reviews(viewModel)
    }
}

//------------------- Restaurants --------------------

@Composable
fun Restaurants(viewModel: OwnerHomeViewModel) {
    val dialogState = remember { mutableStateOf(false) }
    AppDialog(viewModel, dialogState = dialogState)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Title(text = "Your restaurants")

            IconButton(onClick = {
                dialogState.value = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "New Restaurant")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(viewModel.state.value.restaurants) {
                RestaurantItem(restaurant = it)
            }
        }
    }

    /*Column {
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
    }*/
}

@Composable
fun Title(text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.onBackground
    )
}

private fun getFakeRestaurant(): Restaurant {
    val ts = "${System.currentTimeMillis()}"
    return Restaurant(ts, ts, ts, ts, 0f, 0)
}

//------------------- Reviews --------------------

@Composable
fun Reviews(viewModel: OwnerHomeViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Title(text = "New reviews")

        LazyColumn {
            items(state.value.reviews) { review ->
                ReviewItem(review = review)
            }
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
