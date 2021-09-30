package com.lavish.toprestro.old.ui.user.reviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.CardReviewBinding
import com.lavish.toprestro.old.dialogs.ErrorDialog
import com.lavish.toprestro.old.dialogs.OnInputCompleteListener
import com.lavish.toprestro.old.dialogs.TextInputDialog
import com.lavish.toprestro.old.firebaseHelpers.OnCompleteListener
import com.lavish.toprestro.old.firebaseHelpers.admin.ReviewActionsHelper
import com.lavish.toprestro.old.models.Restaurant
import com.lavish.toprestro.old.models.Review
import com.lavish.toprestro.old.ui.user.ListEventCallbacks

class ReviewViewHolder(private val b: CardReviewBinding)
    : RecyclerView.ViewHolder(b.root) {

    private var pos: Int = 0
    private lateinit var restaurant: Restaurant
    private lateinit var listEventCallbacks: ListEventCallbacks

    @SuppressLint("SetTextI18n")
    fun bind(
        restaurant: Restaurant,
        position: Int,
        review: Review,
        isEditMode: Boolean,
        listEventCallbacks: ListEventCallbacks,
    ) {
        this.restaurant = restaurant
        this.pos = position
        this.listEventCallbacks = listEventCallbacks

        //Basic
        b.userNameTv.text = review.userName
        b.dateTv.text = review.formattedDate()
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


        //Review Reply
        if(review.reply != null){
            b.replyTv.text = review.reply
            b.replyGroup.visibility = VISIBLE
        } else {
            b.replyGroup.visibility = GONE
        }

        //Admin Actions
        if(isEditMode){
            b.adminActions.visibility = VISIBLE
            val choices = arrayOf("Review", "Reply")

            b.editBtn.setOnClickListener {
                var checkedItem = 0

                MaterialAlertDialogBuilder(b.root.context)
                        .setTitle("Edit")
                        .setPositiveButton("GO") { _, _ ->
                            edit(b.root.context, choices[checkedItem], review)
                        }
                        .setSingleChoiceItems(choices, 0) { _, which ->
                            checkedItem = which
                        }
                        .show()
            }

            b.deleteBtn.setOnClickListener {
                var checkedItem = 0

                MaterialAlertDialogBuilder(b.root.context)
                        .setTitle("Delete")
                        .setPositiveButton("GO") { _, _ ->
                            when(checkedItem) {
                                0 -> deleteReview(review)
                                1 -> deleteReply(review)
                            }
                        }
                        .setSingleChoiceItems(choices, 0) { _, which ->
                            checkedItem = which
                        }
                        .show()
            }
        }
    }

    private fun deleteReview(review: Review) {
        listEventCallbacks.showLoadingDialog()

        var newNoOfRatings = restaurant.noOfRatings - 1
        if(newNoOfRatings == 0) newNoOfRatings = 1
        val newAvgRating = ((restaurant.avgRating * restaurant.noOfRatings) - review.starRating!!) / newNoOfRatings

        ReviewActionsHelper().deleteReview(restaurant.id!!, review.id!!, newAvgRating, object : OnCompleteListener<Void?> {
            override fun onResult(t: Void?) {
                listEventCallbacks.hideLoadingDialog()
                listEventCallbacks.onDeleted(pos)
            }

            override fun onError(e: String) {
                listEventCallbacks.hideLoadingDialog()
                listEventCallbacks.showErrorDialog(e)
            }

        })
    }

    private fun deleteReply(review: Review) {
        listEventCallbacks.showLoadingDialog()

        ReviewActionsHelper().deleteReply(restaurant.id!!, review.id!!, object : OnCompleteListener<Void?> {
            override fun onResult(t: Void?) {
                listEventCallbacks.hideLoadingDialog()

                review.reply = null

                listEventCallbacks.onModified(pos, review)
            }

            override fun onError(e: String) {
                listEventCallbacks.hideLoadingDialog()
                listEventCallbacks.showErrorDialog(e)
            }

        })
    }

    private fun edit(context: Context, what: String, review: Review) {

        TextInputDialog(context).takeInput("Edit $what", R.drawable.ic_edit, what, EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES, "SAVE", true
                , prefill = if(what == "Review") review.review!! else review.reply ?: ""
                , object : OnInputCompleteListener {
            override fun onSubmit(input: String) {
                val listener = object : OnCompleteListener<Void?> {
                    override fun onResult(t: Void?) {
                        listEventCallbacks.hideLoadingDialog()

                        when(what){
                            "Review" -> review.review = input
                            "Reply" -> review.reply = input
                        }

                        listEventCallbacks.onModified(pos, review)
                    }

                    override fun onError(e: String) {
                        listEventCallbacks.hideLoadingDialog()
                        ErrorDialog(context).show(e)
                    }

                }

                listEventCallbacks.showLoadingDialog()
                when(what){
                    "Review" -> ReviewActionsHelper().editReview(restaurant.id!!, review.id!!, input, listener)
                    "Reply" -> ReviewActionsHelper().editReply(restaurant.id!!, review.id!!, input, listener)
                }
            }
        })
    }
}
