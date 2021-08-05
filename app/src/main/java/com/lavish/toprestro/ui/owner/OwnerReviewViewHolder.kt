package com.lavish.toprestro.ui.owner

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.dialogs.OnInputCompleteListener
import com.lavish.toprestro.dialogs.TextInputDialog
import com.lavish.toprestro.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.firebaseHelpers.admin.ReviewActionsHelper
import com.lavish.toprestro.models.Review

class OwnerReviewViewHolder(private val b: CardReviewBinding)
    : RecyclerView.ViewHolder(b.root) {

    @SuppressLint("SetTextI18n")
    fun bind(review: Review) {
        //Basic
        b.userNameTv.text = review.userName
        b.dateTv.text = "${review.formattedDate()} (${review.restroName})"
        b.reviewTv.text = review.review

        //RatingBar
        b.ratingBar.rating = review.starRating!!.toFloat()
        b.ratingBar.progressTintList = (ColorStateList.valueOf(
                when {
                    review.starRating!! > 4 -> Color.parseColor("#10C300")
                    review.starRating!! > 3 -> Color.parseColor("#82C300")
                    review.starRating!! > 2 -> Color.parseColor("#C3A300")
                    review.starRating!! >= 1 -> Color.parseColor("#C36800")
                    else -> Color.parseColor("#C36800")
                }
        ))
        b.ratingBar.progressBackgroundTintList = b.ratingBar.progressTintList

        //Reply
        if(review.reply == null){
            b.replyGroup.visibility = GONE
            b.replyBtn.visibility = VISIBLE
            b.replyBtn.setOnClickListener {
                handleNewReply(review)
            }
        } else {
            b.replyTv.text = review.reply
            b.replyGroup.visibility = VISIBLE
            b.replyBtn.visibility = GONE
        }
    }

    private fun handleNewReply(review: Review) {
        val app = (b.root.context.applicationContext as App)

        val inputListener = object : OnInputCompleteListener {

            //On reply input done
            override fun onSubmit(input: String) {
                app.showLoadingDialog(b.root.context)

                //On reply sent
                val listener = object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        review.reply = input
                        bind(review)

                        Toast.makeText(b.root.context, "Done!", Toast.LENGTH_SHORT).show()
                        app.hideLoadingDialog()
                    }

                    override fun onError(e: String) {
                        ErrorDialog(b.root.context).show(e.toString())
                    }

                }

                ReviewActionsHelper()
                        .editReply(review.restaurantId!!, review.id!!, input, listener)
            }
        }

        TextInputDialog(b.root.context)
                .takeInput("Reply", R.drawable.ic_reply, "Reply"
                        , EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE, "SEND", true
                        , listener = inputListener)
    }
}
