package com.lavish.toprestro.ui.user.reviews

import android.annotation.SuppressLint
import android.view.View.*
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.models.Review

class ReviewViewHolder(private val b: CardReviewBinding)
    : RecyclerView.ViewHolder(b.root) {

    @SuppressLint("SetTextI18n")
    fun bind(review: Review) {
        //Basic
        b.userNameTv.text = review.userName
        b.dateTv.text = review.formattedDate()
        b.ratingBar.rating = review.starRating!!.toFloat()
        b.reviewTv.text = review.review

        //Review Reply
        if(review.reply != null){
            //TODO : "Owner : "
            b.reviewTv.text = review.reply
            b.replyGroup.visibility = VISIBLE
        } else {
            b.replyGroup.visibility = GONE
        }
    }
}
