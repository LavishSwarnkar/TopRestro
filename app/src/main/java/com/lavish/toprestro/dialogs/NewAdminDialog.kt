package com.lavish.toprestro.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.DialogNewAdminBinding
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.AdminHelper
import com.lavish.toprestro.models.Profile

class NewAdminDialog(val context: Context) {

    lateinit var dialog: AlertDialog
    lateinit var b: DialogNewAdminBinding
    lateinit var listener: OnCompleteListener<Profile>

    fun create(listener: OnCompleteListener<Profile>) {
        this.listener = listener

        //Layout
        b = DialogNewAdminBinding.inflate(LayoutInflater.from(context))

        setupErrorHiders()

        //Show Dialog
        dialog = MaterialAlertDialogBuilder(context, R.style.TopRestroDialog)
                .setView(b.root)
                .show()

        //Button
        b.submitBtn.setOnClickListener {
            onSubmit()
        }
    }

    private fun setupErrorHiders() {
        val watcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateInput()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        }

        b.nameEt.editText?.addTextChangedListener(watcher)
        b.emailIdEt.editText?.addTextChangedListener(watcher)
    }

    private fun onSubmit() {
        if(validateInput()) {
            val name = b.nameEt.editText?.text!!.toString().trim()
            val emailId = b.emailIdEt.editText?.text!!.toString().trim()

            val profile = Profile(name, emailId)

            val context = b.title.context
            val app = context.applicationContext as App
            app.showLoadingDialog(context)

            AdminHelper().createAdmin(profile, object : OnCompleteListener<Void?> {
                override fun onResult(t: Void?) {
                    app.hideLoadingDialog()
                    listener.onResult(profile)
                }

                override fun onError(e: String) {
                    app.hideLoadingDialog()
                    listener.onError(e)
                }
            })

            dialog.dismiss()
        }
    }

    private fun validateInput() : Boolean {
        val isNameValid = !b.nameEt.editText?.text!!.isBlank()
        val isEmailIdValid = !b.nameEt.editText?.text!!.isBlank()

        b.nameEt.error = if(isNameValid) null else "Please enter something"
        b.emailIdEt.error = if(isEmailIdValid) null else "Please enter something"

        return isNameValid && isEmailIdValid
    }
}