package com.lavish.toprestro.dialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.DialogErrorBinding
import com.lavish.toprestro.databinding.DialogTextInputBinding

class ErrorDialog(val context: Context) {

    lateinit var b: DialogErrorBinding

    fun show(error: String) {

        //Layout
        b = DialogErrorBinding.inflate(LayoutInflater.from(context))
        b.errorTv.text = error

        //Show Dialog
        val dialog = MaterialAlertDialogBuilder(context, R.style.TopRestroDialog)
                .setView(b.root)
                .show()

        //Button
        b.okayBtn.setOnClickListener { dialog.dismiss() }
    }

}