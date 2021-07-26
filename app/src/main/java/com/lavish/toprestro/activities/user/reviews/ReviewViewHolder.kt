package com.lavish.toprestro.activities.user.reviews

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.lavish.toprestro.activities.user.reviews.UserRestroActivity
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.Constants

class ReviewViewHolder(private val b: CardReviewBinding)
    : RecyclerView.ViewHolder(b.root) {

    @SuppressLint("SetTextI18n")
    fun bind(review: Review) {
        //Basic
        b.userNameTv.text = review.userName
        b.dateTv.text = review.formattedDate()
        b.ratingBar.rating = review.starRating.toFloat()
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
