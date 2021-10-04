package com.lavish.toprestro.featureOwner.presentation.ownerHome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lavish.compose.ui.theme.TopRestroTheme
import com.lavish.toprestro.R
import com.lavish.toprestro.featureOwner.domain.model.Restaurant
import com.lavish.toprestro.featureOwner.domain.model.Review
import com.lavish.toprestro.featureOwner.presentation.ownerHome.components.*
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
        Scaffold(
            scaffoldState = scaffoldState,
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
fun OwnerHome(viewModel: OwnerHomeViewModel) {
    when (state.value.uiState) {
        is UiState.Loading -> Status("Loading...")
        is UiState.Loaded -> MainView(viewModel)
        is UiState.Error -> Status("Error : ${(state.value.uiState as UiState.Error).e}...")
        is UiState.OfflineError -> OfflineStatus(onRetry = {
            viewModel.onEvent(OwnerHomeEvent.Refresh)
        })
    }
}

@Composable
fun MainView(viewModel: OwnerHomeViewModel) {

    //Dialogs
    val newRestaurantDialogState = remember { mutableStateOf(false) }
    val replyDialogState = remember { mutableStateOf(false) }
    val replyDialogReviewState = remember { mutableStateOf<Review?>(null) }
    Dialogs(viewModel, newRestaurantDialogState, replyDialogState, replyDialogReviewState)

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        //Restaurants Heading
        item {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Title(text = "Your restaurants")

                IconButton(onClick = {
                    newRestaurantDialogState.value = true
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "New Restaurant")
                }
            }
        }

        //Restaurants
        if(state.value.restaurants.isEmpty()) item {
            NoItems(text = stringResource(id = R.string.no_restaurants))
        } else items(state.value.restaurants) {
            RestaurantItem(restaurant = it)
        }

        //Reviews Heading
        item {
            Divider(Modifier.padding(top = 16.dp, bottom = 16.dp))
            Title(text = "New reviews")
        }

        //Reviews
        if(state.value.reviews.isEmpty()) item {
            NoItems(text = stringResource(id = R.string.no_reviews))
        } else items(state.value.reviews) { review ->
            ReviewItem(review = review, onReplyClick = {
                replyDialogState.value = true
                replyDialogReviewState.value = review
            })
        }
    }
}

@Composable
fun Dialogs(
    viewModel: OwnerHomeViewModel,
    newRestaurantDialogState: MutableState<Boolean>,
    replyDialogState: MutableState<Boolean>,
    replyDialogReviewState: MutableState<Review?>
) {
    TextInputDialog(
        dialogState = newRestaurantDialogState,
        title = "New restaurant",
        icon = Icons.Default.Add,
        inputLabel = "Name",
        buttonText = "Create",
        onInput = {
            viewModel.onEvent(OwnerHomeEvent.CreateNewRestaurant(
                Restaurant(
                    id = "",
                    name = it,
                    imageURL = Restaurant.randomRestroImageUrl(),
                    ownerEmail = "", //Will be added in viewmodel
                    avgRating = 0f,
                    noOfRatings = 0
                )
            ))
        }
    )

    TextInputDialog(
        dialogState = replyDialogState,
        title = "Review Reply",
        painterIcon = painterResource(R.drawable.ic_reply),
        inputLabel = "Reply",
        buttonText = "Send",
        onInput = {
            viewModel.onEvent(OwnerHomeEvent.ReplyReview(
                replyDialogReviewState.value!!,
                it
            ))
        }
    )
}

data class ReplyDialogState(
    val visible: Boolean = false,
    val review: Review? = null
)
