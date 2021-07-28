package com.lavish.toprestro.ui.user.reviews

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.databinding.HeaderUserRestroActivityBinding
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.Constants.HEADER
import com.lavish.toprestro.other.Constants.OTHER_ITEMS

class ReviewsAdapter(val context: Context, val restaurant: Restaurant, val reviews: List<Review>, val isRateReviewDone: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == HEADER)
            HeaderViewHolder(
                    HeaderUserRestroActivityBinding.inflate(
                            LayoutInflater.from(context)
                            , parent, false))
        else
            ReviewViewHolder(
                CardReviewBinding.inflate(
                        LayoutInflater.from(context)
                        , parent
                        , false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == 0)
            (holder as HeaderViewHolder).bind(restaurant, isRateReviewDone, reviews.size)
        else
            (holder as ReviewViewHolder).bind(reviews[position - 1])
    }

    override fun getItemViewType(position: Int): Int = if(position == 0) HEADER else OTHER_ITEMS

    override fun getItemCount() = reviews.size + 1

}