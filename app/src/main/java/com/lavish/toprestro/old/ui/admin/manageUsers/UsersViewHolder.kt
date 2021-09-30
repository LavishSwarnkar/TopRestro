package com.lavish.toprestro.old.ui.admin.manageUsers

import android.content.Context
import android.content.DialogInterface
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.old.dialogs.ErrorDialog
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.firebaseHelpers.admin.DeleteHelper
import com.lavish.toprestro.old.models.Profile

class UsersViewHolder(val type: String, val b: CardUserBinding)
    : RecyclerView.ViewHolder(b.root) {

    private lateinit var onDeleted: () -> Unit
    private lateinit var profile: Profile
    private lateinit var context: Context
    private lateinit var app: App

    fun bind(profile: Profile, onDeleted : () -> Unit){
        context = b.root.context
        app = (context.applicationContext as App)
        this.profile = profile
        this.onDeleted = onDeleted

        b.nameTv.text = profile.name

        b.deleteBtn.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(context)
                .setTitle("Confirm delete?")
                .setPositiveButton("YES"){ _: DialogInterface, _: Int ->
                    delete()
                }.setNegativeButton("CANCEL", null)
                .show()
    }

    private fun delete() {
        app.showLoadingDialog(context)

        DeleteHelper()
                .delete(profile.emailId!!, type, object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        app.hideLoadingDialog()
                        onDeleted()
                    }

                    override fun onError(e: String) {
                        app.hideLoadingDialog()
                        ErrorDialog(context).show(e)
                    }
                })
    }

}