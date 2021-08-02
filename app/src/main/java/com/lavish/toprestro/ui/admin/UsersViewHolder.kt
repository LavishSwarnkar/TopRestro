package com.lavish.toprestro.ui.admin

import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.DeleteHelper
import com.lavish.toprestro.models.Profile

class UsersViewHolder(val type: String, val b: CardUserBinding)
    : RecyclerView.ViewHolder(b.root) {

    fun bind(profile: Profile, onDeleted : () -> Unit){
        val context = b.root.context
        val app = (context.applicationContext as App)

        b.nameTv.text = profile.name

        b.deleteBtn.setOnClickListener {
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

}