package com.lavish.toprestro.ui.owner

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.databinding.HeaderOwnerActivityBinding
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.Constants.HEADER
import com.lavish.toprestro.other.Constants.OTHER_ITEMS

class OwnerReviewsAdapter(val context: Context, val reviews: List<Review>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == HEADER)
            OwnerHeaderViewHolder(
                    HeaderOwnerActivityBinding.inflate(
                            LayoutInflater.from(context)
                            , parent, false))
        else
            OwnerReviewViewHolder(
                CardReviewBinding.inflate(
                        LayoutInflater.from(context)
                        , parent
                        , false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position == 0)
            (holder as OwnerHeaderViewHolder).bind()
        else
            (holder as OwnerReviewViewHolder).bind(reviews[position - 1])
    }

    override fun getItemViewType(position: Int): Int = if(position == 0) HEADER else OTHER_ITEMS

    override fun getItemCount() = reviews.size + 1

}