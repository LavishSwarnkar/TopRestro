package com.lavish.toprestro.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.ActivityRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.ui.user.reviews.ReviewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRestroFragment: Fragment() {

    /*@Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory*/

    val userViewModel: UserViewModel by activityViewModels()

    lateinit var b: ActivityRestroBinding
    val args: UserRestroFragmentArgs by navArgs()
    private lateinit var app: App

    private lateinit var restaurant: Restaurant
    private var isEditMode: Boolean = false

    private var reviews: MutableList<Review> = mutableListOf()
    lateinit var adapter: ReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        app = activity?.applicationContext as App
        b = ActivityRestroBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //userViewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        getRestroInfo()
        lifecycleScope.launch {
            fetchReviews()
        }
    }

    private fun getRestroInfo() {
        //From args
        val restroStr : String = args.restaurant
        isEditMode = args.isEditMode
        restaurant = Gson().fromJson(restroStr, Restaurant::class.java)

        //Set title
        (activity as AppCompatActivity).supportActionBar?.title = restaurant.name
    }

    private suspend fun fetchReviews() {
        userViewModel.fetchReviews(restaurant.rId)

        userViewModel.restroUiState.collect {
            when(it) {
                //Loading
                is UiState.Loading -> {
                    app.showLoadingDialog(requireContext())
                }

                //Loading complete
                is UiState.ShowData<*> -> {
                    app.hideLoadingDialog()

                    reviews = it.data as MutableList<Review>
                    setupRecyclerView()
                }

                //User offline
                is UiState.Offline -> {
                    app.hideLoadingDialog()
                    onOffline()
                }

                //Error
                is UiState.Error -> {
                    app.hideLoadingDialog()
                    ErrorDialog(requireContext()).show(it.e)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ReviewsAdapter(restaurant, reviews, isRateReviewDone(), isEditMode)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        app.hideLoadingDialog()
    }

    private fun isRateReviewDone(): Boolean {
        val userEmail = app.profile?.emailId
        var isRateReviewDone = false
        for(review in reviews) {
            if(review.userEmail == userEmail){
                isRateReviewDone = true
                break
            }
        }
        return isRateReviewDone
    }

    private fun onOffline() {
        val onOfflineRetry: () -> Unit = {
            userViewModel.fetchRestros()
        }

        val onOfflineClose: () -> Unit = {
            activity?.finish()
        }

        ErrorDialog(requireContext()).show(
            isOfflineError = true,
            retryCallback = onOfflineRetry,
            closeCallback = onOfflineClose
        )
    }

}