package com.lavish.toprestro.ui.admin.manageRestro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.databinding.CardRestroBinding
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Constants.TYPE_ADMIN
import com.lavish.toprestro.other.LogoutHelper

class ManageRestroAdapter(
        val restaurants: MutableList<Restaurant>,
        val showNoUsersView: (Boolean) -> Unit,
)
            : RecyclerView.Adapter<RestroViewHolder>() {

    var visibleRestaurants: MutableList<Restaurant> = restaurants.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestroViewHolder {
        return RestroViewHolder(
                CardRestroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RestroViewHolder, position: Int) {
        holder.bind(visibleRestaurants[position]) {
            restaurants.remove(visibleRestaurants[position])
            visibleRestaurants.removeAt(position)
            notifyItemRemoved(position)

            //No users
            if(restaurants.size == 0) showNoUsersView(true)
        }
    }

    override fun getItemCount(): Int = visibleRestaurants.size

    fun filter(query: String?) {
        visibleRestaurants = mutableListOf()

        if(query != null){
            restaurants.forEach {
                if(it.name!!.contains(query))
                    visibleRestaurants.add(it)
            }
        }

        showNoUsersView(visibleRestaurants.size == 0)

        notifyDataSetChanged()
    }

}