package com.lavish.toprestro.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var app: App
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        app = applicationContext as App

        setupNavController()
        setupActionBarWithNavController(navController)
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



    //Opened Restro changed JUGAAD

    /*override fun onResume() {
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
    }*/


    //Double tap to Exit

    /*private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }*/

}