package com.lavish.toprestro.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.DialogErrorBinding

class ErrorDialog(val context: Context) {

    lateinit var b: DialogErrorBinding
    lateinit var dialog: AlertDialog

    fun show(
        error: String = "You are offline!",
        isOfflineError: Boolean = false,
        retryCallback: () -> Unit = {},
        closeCallback: () -> Unit = {}
    ) {

        //Layout
        b = DialogErrorBinding.inflate(LayoutInflater.from(context))
        b.errorTv.text = error

        //Show Dialog
        val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.TopRestroDialog)
                .setView(b.root)

        //Offline Error dialog
        if(isOfflineError) {

            //Retry button
            b.posBtn.text = context.getString(R.string.retry)
            b.posBtn.setOnClickListener { retryCallback() }

            //Close button
            b.negBtn.visibility = VISIBLE
            b.negBtn.setOnClickListener { closeCallback() }

            dialog = dialogBuilder.setCancelable(false).show()
        }

        //Normal behaviour
        else {
            dialog = dialogBuilder.show()

            //Okay Button
            b.posBtn.setOnClickListener { dialog.dismiss() }
        }
    }

}