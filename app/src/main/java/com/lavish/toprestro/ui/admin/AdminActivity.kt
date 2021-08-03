package com.lavish.toprestro.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityAdminBinding
import com.lavish.toprestro.other.Constants.*
import com.lavish.toprestro.other.LogoutHelper

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

}