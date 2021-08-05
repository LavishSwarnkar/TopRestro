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

class ReviewsAdapter(val context: Context, val restaurant: Restaurant, val reviews: MutableList<Review>, val isRateReviewDone: Boolean, val isEditMode: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
                            , false),
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == 0)
            (holder as HeaderViewHolder).bind(restaurant, isEditMode or isRateReviewDone, reviews.size)
        else
            (holder as ReviewViewHolder).bind(restaurant, reviews[position - 1], isEditMode, {
                //onDeleted
                reviews.removeAt(position-1)
                notifyItemRemoved(position)

                if(reviews.size == 0)
                    notifyItemChanged(0)
            }) {
                //onModified
                notifyItemChanged(position)
            }
    }

    override fun getItemViewType(position: Int): Int = if(position == 0) HEADER else OTHER_ITEMS

    override fun getItemCount() = reviews.size + 1

}