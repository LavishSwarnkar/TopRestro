package com.lavish.toprestro.ui.user.reviews

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.HeaderUserRestroActivityBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.user.NewReviewHelper
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review

class HeaderViewHolder(val b: HeaderUserRestroActivityBinding) : RecyclerView.ViewHolder(b.root){

    //3.5, 10 -> 4 =>

    private var noOfReviews: Int = 0
    private lateinit var restaurant: Restaurant

    @SuppressLint("SetTextI18n")
    fun bind(restaurant: Restaurant, isRateReviewDone: Boolean, noOfReviews: Int) {
        this.restaurant = restaurant
        this.noOfReviews = noOfReviews

        showRating()

        //Rate Review visibility
        b.rateReviewRoot.visibility = if(isRateReviewDone) GONE else VISIBLE
        b.submitBtn.setOnClickListener { validateRateReviewInput() }

        //Load banner image
        Glide.with(b.root)
                .asBitmap()
                .load(restaurant.imageURL)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        b.banner.setImageBitmap(resource)
                        b.banner.visibility = View.VISIBLE
                        b.loader.visibility = View.GONE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        ErrorDialog(b.root.context).show("Failed to load image!")
                    }
                })

        //Handle 0 reviews case
        handleReviewsVisibility()
    }

    private fun showRating() {
        b.rating.apply {
            backgroundTintList = (ColorStateList.valueOf(
                    when {
                        restaurant.avgRating > 4 -> Color.parseColor("#10C300")
                        restaurant.avgRating > 3 -> Color.parseColor("#82C300")
                        restaurant.avgRating > 2 -> Color.parseColor("#C3A300")
                        restaurant.avgRating >= 1 -> Color.parseColor("#C36800")
                        else -> Color.parseColor("#C36800")
                    }
            ))

            if (restaurant.noOfRatings > 0){
                text = "${String.format("%.1f", restaurant.avgRating)} ★"
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }

    private fun validateRateReviewInput() {
        val context = b.root.context
        val app = context.applicationContext as App

        val rating = b.ratingBar.rating
        val reviewStr = b.editText.editText!!.text.toString()

        when {
            rating == 0f -> Toast.makeText(context, "Please select a rating first!", Toast.LENGTH_SHORT).show()
            reviewStr.isBlank() -> Toast.makeText(context, "Please write a review also!", Toast.LENGTH_SHORT).show()

            else -> {
                app.showLoadingDialog(context)

                val profile = app.profile
                val review = Review(userName = profile!!.name,
                        userEmail = profile.emailId,
                        restaurantId = restaurant.id!!,
                        ownerEmail = restaurant.ownerEmail,
                        starRating = rating,
                        review = reviewStr)

                val listener = object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        app.hideLoadingDialog()
                        noOfReviews++
                        handleReviewsVisibility()
                        onReviewDone(review)
                        showRating()
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(context).show(e)
                    }
                }

                //3.5, 10 + 4 = 39/11
                //7.5/11

                restaurant.noOfRatings++
                restaurant.avgRating = (restaurant.avgRating * (restaurant.noOfRatings-1) + rating) / restaurant.noOfRatings

                NewReviewHelper()
                        .saveNewReview(restaurant.id!!, restaurant.avgRating, review, listener)
            }
        }
    }

    private fun onReviewDone(review: Review) {
        b.rateReviewRoot.visibility = GONE
        (b.root.context as RestroActivity).showNewReview(restaurant, review)
    }

    private fun handleReviewsVisibility() {
        if(noOfReviews == 0){
            b.pastReviewsTitle.visibility = GONE
            b.noReviews.visibility = VISIBLE
        } else {
            b.pastReviewsTitle.visibility = VISIBLE
            b.noReviews.visibility = GONE
        }
    }

}

private fun RestroActivity.showNewReview(restaurant: Restaurant, review: Review) {
    reviews.add(0, review)
    adapter.notifyItemInserted(1)
    (applicationContext as App).updatedRestro = restaurant
}


