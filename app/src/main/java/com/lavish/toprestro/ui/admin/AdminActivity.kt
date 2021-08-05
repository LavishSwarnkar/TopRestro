package com.lavish.toprestro.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityAdminBinding
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.AdminHelper
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.other.Constants.*
import com.lavish.toprestro.other.LogoutHelper
import com.lavish.toprestro.ui.admin.manageRestro.ManageRestroActivity
import com.lavish.toprestro.ui.admin.manageUsers.ManageUsersActivity

class AdminActivity : AppCompatActivity() {


    private lateinit var app: App
    private lateinit var b: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = (applicationContext as App)

        checkIfAdminHasAccess()
    }

    private fun checkIfAdminHasAccess() {
        app.showLoadingDialog(this)

        AdminHelper()
                .checkIfAdmin(app.profile!!.emailId!!, object : OnCompleteListener<Profile?> {
                    override fun onResult(t: Profile?) {
                        app.hideLoadingDialog()

                        //Negative
                        if(t == null) onAccessDenied()
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        Toast.makeText(this@AdminActivity, "Something went wrong!", Toast.LENGTH_SHORT).show()
                        finish()
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

    fun manageUsers(view: View) { manage(TYPE_USER) }

    fun manageOwners(view: View) { manage(TYPE_OWNER) }

    fun manageAdmins(view: View) { manage(TYPE_ADMIN) }

    fun manageRestaurants(view: View) =
        startActivity(Intent(this, ManageRestroActivity::class.java))

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