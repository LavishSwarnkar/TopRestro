package com.lavish.toprestro.ui.user.reviews

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.ActivityRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.user.ReviewsFetcher
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.EDIT_RESTAURANT
import com.lavish.toprestro.other.RESTAURANT_INFO_KEY

class RestroActivity : AppCompatActivity() {

    lateinit var adapter: ReviewsAdapter
    lateinit var reviews: MutableList<Review>
    private lateinit var app: App
    lateinit var restaurant: Restaurant
    lateinit var b: ActivityRestroBinding
    var isEditMode: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityRestroBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = application as App

        getRestroInfo()
        fetchReviews()
    }

    private fun getRestroInfo() {
        val restroStr : String = intent.extras!![RESTAURANT_INFO_KEY] as String
        isEditMode = intent.extras!![EDIT_RESTAURANT] as Boolean
        restaurant = Gson().fromJson(restroStr, Restaurant::class.java)

        //Set title
        title = restaurant.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fetchReviews() {
        app.showLoadingDialog(this)

        ReviewsFetcher()
                .fetch(restaurant.id!!, object : OnCompleteListener<MutableList<Review>>{
                    override fun onResult(t: MutableList<Review>) {
                        reviews = t
                        setupRecyclerView()
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        reviews = mutableListOf()
                        ErrorDialog(this@RestroActivity).show(e)
                    }
                })
    }

    private fun setupRecyclerView() {
        //Check if reviews contain this user's review
        val userEmail = app.profile?.emailId
        var isRateReviewDone = false
        for(review in reviews) {
            if(review.userEmail == userEmail){
                isRateReviewDone = true
                break
            }
        }

        adapter = ReviewsAdapter(this, restaurant, reviews, isRateReviewDone, isEditMode ?: false)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        app.hideLoadingDialog()
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