package com.lavish.toprestro.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityAdminBinding
import com.lavish.toprestro.other.Constants.*

class AdminActivity : AppCompatActivity() {


    private lateinit var b: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(b.root)
    }

    fun manageUsers(view: View) { manage(TYPE_USER) }

    fun manageOwners(view: View) { manage(TYPE_OWNER) }

    fun manageAdmins(view: View) { manage(TYPE_ADMIN) }

    fun manageRestaurants(view: View) {}

    private fun manage(type: String) {
        val intent = Intent(this, ManageUsersActivity::class.java)
        intent.putExtra("type", type)
        startActivity(intent)
    }

}