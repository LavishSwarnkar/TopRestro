package com.lavish.toprestro.activities.owner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lavish.toprestro.databinding.CardRestroBinding
import com.lavish.toprestro.databinding.HeaderOwnerActivityBinding
import com.lavish.toprestro.databinding.HeaderUserRestroActivityBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Prefs

class OwnerHeaderViewHolder(val b: HeaderOwnerActivityBinding) : RecyclerView.ViewHolder(b.root){

    @SuppressLint("SetTextI18n")
    fun bind() {
        //Get saved restaurants
        val restaurants = Prefs(b.root.context).getRestros()

        inflateRestaurantCards(restaurants)

        setupNewRestaurantsHandler()
    }

    private fun setupNewRestaurantsHandler() {
        b.newRestaurantBtn.setOnClickListener {
            (b.root.context as OwnerActivity).showNewRestaurantDialog()
        }
    }

    private fun inflateRestaurantCards(restaurants: List<Restaurant>) {
        if(restaurants.isEmpty()){
            b.restaurantsList.visibility = GONE
            b.newReviewsTitle.visibility = GONE
            b.noRestaurants.visibility = VISIBLE
            return
        }

        b.restaurantsList.visibility = VISIBLE
        b.newReviewsTitle.visibility = VISIBLE
        b.noRestaurants.visibility = GONE

        restaurants.forEach { restaurant ->
            val binding = CardRestroBinding.inflate(LayoutInflater.from(b.root.context), b.restaurantsList, false)
            binding.nameTv.text = restaurant.name

            b.restaurantsList.addView(binding.root)
        }
    }

}