package com.lavish.toprestro.activities.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lavish.toprestro.databinding.ActivityMainBinding
import com.lavish.toprestro.models.Restaurant
import java.util.*
import kotlin.Comparator

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: RestaurantsAdapter
    lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        testList()
        setupFilterHandler()
    }

    private fun setupFilterHandler() {
        b.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            adapter.currMinStarRating = rating.toInt()
        }
    }

    private fun testList() {
        val restros = dummyRestros().sortedWith {
            o1, o2 -> o2!!.avgRating.compareTo(o1!!.avgRating)
        }.toMutableList()

        adapter = RestaurantsAdapter(this, restros)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter
    }

    private fun dummyRestros(): List<Restaurant> {
        return listOf(
                Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=1", 4.34444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=2", 3.4444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=3", 2.64444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=4", 4.74444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=5", 4f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=6", 0f, 0)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=7", 2.64444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=8", 1.74444f, 5)
                , Restaurant("Appu da Dhaba", "https://picsum.photos/1920/1080?type=9", 3.84444f, 5)
        )
    }

}