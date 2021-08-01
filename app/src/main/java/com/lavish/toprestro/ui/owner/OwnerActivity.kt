package com.lavish.toprestro.ui.owner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.ActivityOwnerBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.dialogs.NewRestaurantDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.owner.NewReviewsFetcher
import com.lavish.toprestro.models.Review
import com.lavish.toprestro.other.LogoutHelper

class OwnerActivity : AppCompatActivity() {

    private lateinit var app: App
    lateinit var adapter: OwnerReviewsAdapter
    lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>
    private lateinit var b: ActivityOwnerBinding
    var newRestaurantDialog: NewRestaurantDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(b.root)
        app = (application as App)

        setupHandlerForImagePicker()
        fetchReviews()
    }

    private fun fetchReviews() {
        app.showLoadingDialog(this)

        val ownerEmail = FirebaseAuth.getInstance().currentUser!!.email
        NewReviewsFetcher()
                .fetch(ownerEmail!!, object : OnCompleteListener<List<Review>> {
                    override fun onResult(t: List<Review>) {
                        app.hideLoadingDialog()
                        setupRecyclerView(t)
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(this@OwnerActivity).show(e)
                    }
                })
    }

    private fun setupHandlerForImagePicker() {
        startForProfileImageResult =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                    val resultCode = result.resultCode
                    val data = result.data

                    if (resultCode == Activity.RESULT_OK) {
                        val imageUri = data?.data!!
                        newRestaurantDialog!!.onImagePicked(imageUri)
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        ErrorDialog(this).show(ImagePicker.getError(data))
                    }
                }
    }

    private fun setupRecyclerView(reviews: List<Review>) {
        adapter = OwnerReviewsAdapter(this, reviews)

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter
    }

    private fun dummyReviews(): List<Review> {
        return emptyList()
    }

    fun showNewRestaurantDialog() {
        newRestaurantDialog = NewRestaurantDialog(this).show()
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