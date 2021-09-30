package com.lavish.toprestro.old.ui.admin.manageRestro

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityManageUsersBinding
import com.lavish.toprestro.old.dialogs.ErrorDialog
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.firebaseHelpers.common.AllRestaurantsFetcher
import com.lavish.toprestro.old.models.Restaurant

class ManageRestroActivity : AppCompatActivity() {

    private var adapter: ManageRestroAdapter? = null
    private lateinit var app: App
    private lateinit var b: ActivityManageUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Common
        b = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = (application as App)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //title & text
        title = "Manage Restaurants"
        b.noUsersFound.text = "No restaurants found!"

        fetchData()
    }

    private fun fetchData() {
        app.showLoadingDialog(this)

        AllRestaurantsFetcher()
                .fetch(object : OnCompleteListener<MutableList<Restaurant>> {
                    override fun onResult(t: MutableList<Restaurant>) {
                        app.hideLoadingDialog()
                        val restros = t.sortedWith (
                            compareBy { it.name }
                        )
                        setupAdapter(restros.toMutableList())
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(this@ManageRestroActivity).show(e)
                    }
                })
    }

    private fun setupAdapter(profiles: MutableList<Restaurant>) {
        if(profiles.size == 0){
            b.noUsersFound.visibility = VISIBLE
        }

        adapter = ManageRestroAdapter(profiles.toMutableList()) {
            b.noUsersFound.visibility = if(it) VISIBLE else GONE
        }

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        app.hideLoadingDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        (menu!!.findItem(R.id.search).actionView as SearchView)
                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if(adapter != null)
                            adapter?.filter(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if(adapter != null)
                            adapter?.filter(newText)
                        return true
                    }
                })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.search -> {

                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}