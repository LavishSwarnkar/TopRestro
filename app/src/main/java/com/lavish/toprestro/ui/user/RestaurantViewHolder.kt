package com.lavish.toprestro.ui.user

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant

class RestaurantViewHolder(private val b: CardUserRestroBinding)
    : RecyclerView.ViewHolder(b.root) {

    @SuppressLint("SetTextI18n")
    fun bind(restaurant: Restaurant) {
        resetImage()

        //Show Name & Image
        loadImage(restaurant.imageURL!!)
        b.name.text = restaurant.name

        //Rating
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
                visibility = VISIBLE
            } else {
                visibility = GONE
            }
        }

        //OnClickHandler
        b.root.setOnClickListener {
            RestroListFragmentDirections.actionRestroListFragmentToUserRestroFragment(
                Gson().toJson(restaurant)
            )
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
