package com.lavish.toprestro.ui.admin.manageRestro

import android.content.Context
import android.content.DialogInterface
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.CardRestroBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.DeleteHelper
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.TYPE_RESTAURANT

class RestroViewHolder(val b: CardRestroBinding)
    : RecyclerView.ViewHolder(b.root) {

    private lateinit var onDeleted: () -> Unit
    private lateinit var restaurant: Restaurant
    private lateinit var context: Context
    private lateinit var app: App

    fun bind(restaurant: Restaurant, onDeleted : () -> Unit){
        context = b.root.context
        app = (context.applicationContext as App)
        this.restaurant = restaurant
        this.onDeleted = onDeleted

        b.nameTv.text = restaurant.name

        b.editBtn.visibility = VISIBLE
        b.deleteBtn.visibility = VISIBLE

        b.editBtn.setOnClickListener {
            //TODO
            /*val intent = Intent(context, RestroActivity::class.java)
            intent.putExtra(RESTAURANT_INFO_KEY, Gson().toJson(restaurant))
            intent.putExtra(EDIT_RESTAURANT, true)
            context.startActivity(intent)*/
        }

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
                .delete(restaurant.id!!, TYPE_RESTAURANT, object : OnCompleteListener<Void?> {
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