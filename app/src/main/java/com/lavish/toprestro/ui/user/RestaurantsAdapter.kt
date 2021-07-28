package com.lavish.toprestro.ui.user

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardUserRestroBinding
import com.lavish.toprestro.models.Restaurant

class RestaurantsAdapter(val context: Context, private var restaurants: MutableList<Restaurant>) : RecyclerView.Adapter<RestaurantViewHolder>() {

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
        if(restaurants.size == 0)
            (context as MainActivity).showNothingFoundView(true)
        else
            (context as MainActivity).showNothingFoundView(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
                CardUserRestroBinding.inflate(
                        LayoutInflater.from(context)
                        , parent
                        , false))
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount() = restaurants.size

}

private fun MainActivity.showNothingFoundView(show: Boolean) {
    b.nothingFound.visibility = if(show) VISIBLE else GONE
}
