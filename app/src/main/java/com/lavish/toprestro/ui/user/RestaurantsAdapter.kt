package com.lavish.toprestro.ui.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.models.Restaurant

class RestaurantsAdapter(
    private var restaurants: MutableList<Restaurant>,
    val onNothingFound: (Boolean) -> Unit
) : ListAdapter<Restaurant, RestaurantViewHolder>(diffCallback) {

    var allRestaurants : List<Restaurant> = restaurants.toList()

    var currMinStarRating = 0
        set(value) {
            field = value
            filter()
        }

    init {
        filter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
                CardUserRestroBinding.inflate(
                        LayoutInflater.from(parent.context)
                        , parent
                        , false))
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount() = restaurants.size

    fun updateList(data: MutableList<Restaurant>) {
        allRestaurants = data
        filter()
    }

    fun filter() {
        restaurants = mutableListOf()

        allRestaurants.forEach {
            if(it.avgRating >= currMinStarRating)
                restaurants.add(it)
        }

        restaurants.sortByDescending { it.avgRating }
        submitList(restaurants)
        Log.d("MyRestroAdapter", "performed filtering, size: ${restaurants.size}/${allRestaurants.size}!")

        //Handle 0 case
        onNothingFound(restaurants.size == 0)
    }

}

object diffCallback: DiffUtil.ItemCallback<Restaurant>() {

    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

}
