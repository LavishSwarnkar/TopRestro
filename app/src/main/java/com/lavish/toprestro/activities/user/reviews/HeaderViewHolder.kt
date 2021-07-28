package com.lavish.toprestro.activities.user.reviews

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lavish.toprestro.databinding.HeaderUserRestroActivityBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant

class HeaderViewHolder(val b: HeaderUserRestroActivityBinding) : RecyclerView.ViewHolder(b.root){

    @SuppressLint("SetTextI18n")
    fun bind(restaurant: Restaurant) {
        //Show rating
        b.rating.apply {
            if (restaurant.noOfRatings > 0){
                text = "${String.format("%.1f", restaurant.avgRating)} ★"
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        //TODO : Show rate & review only if not yet reviewed

        //Load banner image
        Glide.with(b.root)
                .asBitmap()
                .load(restaurant.imageURL + "56")
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
    }

}