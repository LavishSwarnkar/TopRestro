package com.lavish.toprestro.ui.admin.manageRestro

import android.content.Intent
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lavish.toprestro.App
import com.lavish.toprestro.databinding.CardRestroBinding
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.DeleteHelper
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.Constants
import com.lavish.toprestro.other.Constants.TYPE_RESTAURANT
import com.lavish.toprestro.ui.user.reviews.RestroActivity

class RestroViewHolder(val b: CardRestroBinding)
    : RecyclerView.ViewHolder(b.root) {

    fun bind(restaurant: Restaurant, onDeleted : () -> Unit){
        val context = b.root.context
        val app = (context.applicationContext as App)

        b.nameTv.text = restaurant.name

        b.editBtn.visibility = VISIBLE
        b.deleteBtn.visibility = VISIBLE

        b.editBtn.setOnClickListener {
            val intent = Intent(context, RestroActivity::class.java)
            intent.putExtra(Constants.RESTAURANT_INFO_KEY, Gson().toJson(restaurant))
            intent.putExtra(Constants.EDIT_RESTAURANT, true)
            context.startActivity(intent)
        }

        b.deleteBtn.setOnClickListener {
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

}