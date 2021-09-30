package com.lavish.toprestro.old.ui.user.reviews

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.databinding.HeaderUserRestroActivityBinding
import com.lavish.toprestro.old.models.Profile
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.models.Review
import com.lavish.toprestro.old.other.HEADER
import com.lavish.toprestro.old.other.OTHER_ITEMS
import com.lavish.toprestro.old.ui.user.ListEventCallbacks

const val TAG = "MyReviewsAdapter"

class ReviewsAdapter(
    val restaurant: Restaurant,
    val reviews: MutableList<Review>,
    val isRateReviewDone: Boolean,
    val isEditMode: Boolean,
    val userProfile: Profile,
    val listEventCallbacks: ListEventCallbacks
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == HEADER)
            HeaderViewHolder(
                    HeaderUserRestroActivityBinding.inflate(
                            LayoutInflater.from(parent.context)
                            , parent, false))
        else
            ReviewViewHolder(
                    CardReviewBinding.inflate(
                            LayoutInflater.from(parent.context)
                            , parent
                            , false),
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() called with: position = $position")

        if(position == 0)
            (holder as HeaderViewHolder).bind(
                restaurant,
                isEditMode or isRateReviewDone,
                reviews.size,
                listEventCallbacks,
                userProfile
            )
        else
            (holder as ReviewViewHolder).bind(
                restaurant,
                position - 1,
                reviews[position - 1],
                isEditMode,
                listEventCallbacks)
    }

    override fun getItemViewType(position: Int): Int = if(position == 0) HEADER else OTHER_ITEMS

    override fun getItemCount(): Int = reviews.size + 1

}