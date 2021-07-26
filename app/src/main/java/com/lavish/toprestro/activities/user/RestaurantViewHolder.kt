package com.lavish.toprestro.activities.user

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
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Constants

class RestaurantViewHolder(private val b: CardUserRestroBinding)
    : RecyclerView.ViewHolder(b.root) {

    @SuppressLint("SetTextI18n")
    fun bind(restaurant: Restaurant) {
        resetImage()

        //Show Name & Image
        loadImage(restaurant.imageURL)
        b.name.text = restaurant.name

        //Rating
        //TODO : Color
        b.rating.apply {
            if (restaurant.noOfRatings > 0){
                text = "${String.format("%.1f", restaurant.avgRating)} ★"
                visibility = VISIBLE
            } else {
                visibility = GONE
            }
        }

        //OnClickHandler
        b.root.setOnClickListener {
            (b.root.context as MainActivity).openRestaurant(restaurant)
        }
    }

    private fun resetImage() {
        b.loader.visibility = VISIBLE
        b.banner.visibility = GONE
    }

    private fun loadImage(imageURL: String) {
        Glide.with(b.root)
                .asBitmap()
                .load(imageURL)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        b.banner.setImageBitmap(resource)
                        b.banner.visibility = VISIBLE
                        b.loader.visibility = GONE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        ErrorDialog(b.root.context).show("Failed to load image!")
                    }
                })
    }
}

private fun MainActivity.openRestaurant(restaurant: Restaurant) {
    val intent = Intent(this, UserRestroActivity::class.java)
    intent.putExtra(Constants.RESTAURANT_INFO_KEY, Gson().toJson(restaurant))
    startActivity(intent)
}
