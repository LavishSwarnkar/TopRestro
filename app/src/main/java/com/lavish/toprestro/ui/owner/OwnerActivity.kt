package com.lavish.toprestro.ui.owner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.lavish.toprestro.databinding.ActivityOwnerBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.dialogs.NewRestaurantDialog
import com.lavish.toprestro.models.Review

class OwnerActivity : AppCompatActivity() {

    lateinit var adapter: OwnerReviewsAdapter
    lateinit var startForProfileImageResult: ActivityResultLauncher<Intent>
    private lateinit var b: ActivityOwnerBinding
    var newRestaurantDialog: NewRestaurantDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityOwnerBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupHandlerForImagePicker()
        setupRecyclerView()
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

    private fun setupRecyclerView() {
        adapter = OwnerReviewsAdapter(this, dummyReviews())

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

}