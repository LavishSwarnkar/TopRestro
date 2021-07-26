package com.lavish.toprestro.activities.user.reviews

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lavish.toprestro.databinding.ActivityUserRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.Constants

class UserRestroActivity : AppCompatActivity() {

    lateinit var restaurant: Restaurant
    lateinit var b: ActivityUserRestroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityUserRestroBinding.inflate(layoutInflater)
        setContentView(b.root)

        getRestroInfo()
        setupRecyclerView()
    }

    private fun getRestroInfo() {
        val restroStr : String = intent.extras!![Constants.RESTAURANT_INFO_KEY] as String
        restaurant = Gson().fromJson(restroStr, Restaurant::class.java)

        //Set title
        title = restaurant.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val adapter = ReviewsAdapter(this, restaurant, dummyReviews())

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter
    }

    private fun dummyReviews(): List<Review> {
        val s = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."

        return listOf(
                Review("Suresh", s, null, 4)
                , Review("Manish", s, s, 3)
                , Review("Kuriya", s, null, 2)
                , Review("Nagji", s, null, 1)
                , Review("Mohan", s, s, 5)
                , Review("Koyalat", s, null, 4)
        )
    }

    private fun showError(e: String) {
        ErrorDialog(this)
                .show(e)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home){
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}