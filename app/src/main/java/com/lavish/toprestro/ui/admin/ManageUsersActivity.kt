package com.lavish.toprestro.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.ActivityManageUsersBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.UsersFetcher
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.ui.user.reviews.ReviewsAdapter

class ManageUsersActivity : AppCompatActivity() {

    private lateinit var adapter: ManageUsersAdapter
    private lateinit var type: String
    private lateinit var app: App
    private lateinit var b: ActivityManageUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityManageUsersBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = (application as App)
        type = intent.getStringExtra("type")!!

        fetchData()
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
        adapter = ManageUsersAdapter(type, profiles)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        app.hideLoadingDialog()
    }

}