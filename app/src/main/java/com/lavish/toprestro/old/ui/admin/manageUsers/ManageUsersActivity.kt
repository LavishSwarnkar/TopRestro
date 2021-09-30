package com.lavish.toprestro.old.ui.admin.manageUsers

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.lavish.toprestro.old.dialogs.NewAdminDialog
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.firebaseHelpers.admin.UsersFetcher
import com.lavish.toprestro.old.models.Profile
import com.lavish.toprestro.old.other.TYPE_ADMIN
import com.lavish.toprestro.old.other.TYPE_OWNER
import com.lavish.toprestro.old.other.TYPE_USER

class ManageUsersActivity : AppCompatActivity() {

    private var adapter: ManageUsersAdapter? = null
    private lateinit var type: String
    private lateinit var app: App
    private lateinit var b: ActivityManageUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Common
        b = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = (application as App)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //getType & setTitle
        type = intent.getStringExtra("type")!!
        showTitle()

        //FAB visibility
        b.addFab.visibility = if(type == TYPE_ADMIN) VISIBLE else GONE

        fetchData()
    }

    private fun showTitle() {
        title = when(type) {
            TYPE_USER -> "Manage Users"
            TYPE_OWNER -> "Manage Owners"
            TYPE_ADMIN -> "Manage Admins"
            else -> ""
        }
    }

    private fun fetchData() {
        app.showLoadingDialog(this)

        UsersFetcher()
                .fetch(type, object : OnCompleteListener<List<Profile>> {
                    override fun onResult(t: List<Profile>) {
                        app.hideLoadingDialog()
                        setupAdapter(t.toMutableList())
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(this@ManageUsersActivity).show(e)
                    }
                })
    }

    private fun setupAdapter(profiles: MutableList<Profile>) {
        if(profiles.size == 0){
            b.noUsersFound.visibility = VISIBLE
        }

        adapter = ManageUsersAdapter(type, profiles) {
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

    fun newAdmin(view: View) {
        NewAdminDialog(this)
                .create(object : OnCompleteListener<Profile> {
                    override fun onResult(t: Profile) {
                        if(adapter == null){
                            setupAdapter(mutableListOf(t))
                        } else {
                            adapter!!.add(t)
                        }
                    }

                    override fun onError(e: String) {
                        ErrorDialog(this@ManageUsersActivity).show(e)
                    }

                })
    }

}