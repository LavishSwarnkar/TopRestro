package com.lavish.toprestro.featureOwner.presentation.dialogs

import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.DialogNewRetaurantBinding

class NewRestaurantDialog(val context: Context) {

    private lateinit var app: App
    private lateinit var dialog: AlertDialog
    private lateinit var b: DialogNewRetaurantBinding
    private var imageUri: Uri? = null

    fun show(): NewRestaurantDialog {

        //Layout
        b = DialogNewRetaurantBinding.inflate(LayoutInflater.from(context))
        app = (b.root.context.applicationContext as App)

        //Name EditText
        b.nameEt.editText?.apply {
            addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    validateName()
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })
        }

        //Show Dialog
        dialog = MaterialAlertDialogBuilder(context, R.style.TopRestroDialog)
                .setView(b.root)
                .show()

        //Buttons
        b.editImageFAB.setOnClickListener {
            //TODO: (b.root.context as OwnerActivity).openImagePicker()
        }
        b.addBtn.setOnClickListener { createNewRestaurant() }

        return this
    }

    fun onImagePicked(imageUri: Uri) {
        this.imageUri = imageUri

        Glide.with(b.root.context)
                .load(imageUri)
                .into(b.banner)

        b.loader.visibility = GONE
    }

    private fun validate() : Boolean {
        if(imageUri == null)
            Toast.makeText(b.root.context, "Please add a banner image!", Toast.LENGTH_SHORT).show()

        return validateName() && imageUri != null
    }

    private fun validateName() : Boolean {
        return if(b.nameEt.editText?.text!!.isBlank()){
            b.nameEt.error =  "Please enter something"
            false
        } else {
            b.nameEt.error = null
            true
        }
    }

    private fun createNewRestaurant() {
        if(validate()){
            app.showLoadingDialog(b.root.context)
            //TODO: uploadImage()
            dialog.dismiss()
        }
    }

    /*private fun uploadImage() {
        val emailId = app.profile!!.emailId
        val restroName = b.nameEt.editText!!.text.trim().toString()

        val listener = object : OnCompleteListener<String> {
            override fun onResult(t: String) {
                val restro = Restaurant(id = "",
                        name = restroName,
                        ownerEmail = emailId,
                        imageURL = t)
                uploadRestro(emailId!!, restro)
            }

            override fun onError(e: String) {
                app.hideLoadingDialog()
                ErrorDialog(b.root.context).show(e)
            }
        }

        RestaurantImageUploader()
                .uploadImage(imageUri!!
                        , emailId!!
                        , restroName
                        , listener)
    }

    private fun uploadRestro(emailId: String, restro: Restaurant) {
        NewRestaurantHelper()
                .save(emailId, restro, object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        app.hideLoadingDialog()
                        saveRestroLocallyAndReload(restro)
                        Toast.makeText(b.root.context, "Created new restaurant!", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(b.root.context).show(e)
                    }
                })
    }

    private fun saveRestroLocallyAndReload(restro: Restaurant) {

        Prefs(b.root.context).saveNewRestro(restro)
        (b.root.context as OwnerActivity).refreshHeader()
    }*/
}

/*private fun OwnerActivity.refreshHeader() {
    adapter.notifyItemChanged(0)
}

private fun OwnerActivity.openImagePicker() {
    ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }*/

