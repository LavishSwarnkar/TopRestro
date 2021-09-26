package com.lavish.toprestro.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.models.Restaurant

class RestaurantsAdapter(
    private var restaurants: MutableList<Restaurant>,
    val onNothingFound: (Boolean) -> Unit
) : RecyclerView.Adapter<RestaurantViewHolder>() {

    var allRestaurants : List<Restaurant> = restaurants.toList()

    var currMinStarRating = 0
        set(value) {
            field = value
            filter()
        }

    private fun filter() {
        restaurants = mutableListOf()
        allRestaurants.forEach {
            if(it.avgRating >= currMinStarRating)
                restaurants.add(it)
        }

        notifyDataSetChanged()

        //Handle 0 case
        onNothingFound(restaurants.size == 0)
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

}
