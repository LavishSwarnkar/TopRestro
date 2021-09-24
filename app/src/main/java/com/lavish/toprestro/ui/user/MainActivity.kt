package com.lavish.toprestro.ui.user

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityMainBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.common.AllRestaurantsFetcher
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.ACCESS_DENIED
import com.lavish.toprestro.other.LogoutHelper

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
                        if(e == ACCESS_DENIED) {
                            onAccessDenied()
                        } else {
                            app.hideLoadingDialog()
                            ErrorDialog(this@MainActivity).show(e)
                        }
                    }
                })
    }

    private fun onAccessDenied() {
        MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("Access Denied!")
                .setMessage("Your account has been deleted. You have been logged out!")
                .setPositiveButton("OKAY") { dialog, which ->
                    dialog.dismiss()
                    LogoutHelper().logout(this)
                }.show()

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

                    restros = restros!!.sortedWith { o1, o2 ->
                        if (o2.avgRating == o1.avgRating)
                            return@sortedWith o1.name!!.compareTo(o2.name!!)
                        o2.avgRating.compareTo(o1.avgRating)
                    }.toMutableList()

                    setupAdapter(restros!!)

                    app.updatedRestro = null
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            LogoutHelper()
                    .logout(this)
        }

        return super.onOptionsItemSelected(item)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

}