package com.lavish.toprestro.ui.user

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.ActivityMainBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.common.AllRestaurantsFetcher
import com.lavish.toprestro.models.Restaurant

class MainActivity : AppCompatActivity() {
    var restros: MutableList<Restaurant>? = null
    private lateinit var app: App
    private lateinit var adapter: RestaurantsAdapter
    lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = applicationContext as App

        fetchRestros()
    }

    private fun fetchRestros() {
        app.showLoadingDialog(this)

        AllRestaurantsFetcher()
                .fetch(object : OnCompleteListener<MutableList<Restaurant>> {
                    override fun onResult(t: MutableList<Restaurant>) {
                        restros = t
                        setupAdapter(t)
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(this@MainActivity)
                                .show(e!!)
                    }
                })
    }

    private fun setupFilterHandler() {
        b.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            adapter.currMinStarRating = rating.toInt()
        }
    }

    private fun setupAdapter(restros: MutableList<Restaurant>) {
        if(restros.size == 0){
            app.hideLoadingDialog()
            b.nothingFound.visibility = VISIBLE
            b.filterHeader.visibility = GONE
            return
        }

        adapter = RestaurantsAdapter(this, restros)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        setupFilterHandler()
        app.hideLoadingDialog()
    }

    override fun onResume() {
        super.onResume()

        val updatedRestro = (application as App).updatedRestro
        if(updatedRestro != null && restros != null){
            for(i in 0 until restros!!.size) {
                val restaurant = restros!![i]
                if(restaurant.id == updatedRestro.id){
                    restros!!.removeAt(i)
                    restros!!.add(i, updatedRestro)
                    setupAdapter(restros!!)

                    app.updatedRestro = null
                }
            }
        }
    }

}