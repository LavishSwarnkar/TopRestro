package com.lavish.toprestro.dialogs

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.DialogTextInputBinding

class TextInputDialog(val context: Context) {

    lateinit var dialog: AlertDialog
    lateinit var b: DialogTextInputBinding
    lateinit var listener: OnInputCompleteListener

    fun takeInput(title: String, icon: Int, inputHint: String, type: Int, buttonText: String, cancellable: Boolean, prefill: String = "", listener: OnInputCompleteListener) {

        this.listener = listener

        //Layout
        b = DialogTextInputBinding.inflate(LayoutInflater.from(context))

        //Title & icon
        b.title.text = title
        b.icon.setImageResource(icon)

        //EditText
        b.editText.hint = inputHint
        b.editText.editText!!.setText(prefill)
        b.editText.editText?.apply {

            if(type == EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE){
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE
                isSingleLine = false
            } else
                inputType = type

            addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    validateInput()
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            })
        }

        //Show Dialog
        dialog = MaterialAlertDialogBuilder(context, R.style.TopRestroDialog)
                .setCancelable(cancellable)
                .setView(b.root)
                .show()

        //Button
        b.submitBtn.apply {
            text = buttonText
            setOnClickListener { onSubmit() }
        }
    }

    private fun onSubmit() {
        if(validateInput()){
            val input = b.editText.editText?.text!!.toString().trim()
            listener.onSubmit(input)
            dialog.dismiss()
        }
    }

    private fun validateInput() : Boolean {
        val isValid = !b.editText.editText?.text!!.isBlank()

        b.editText.error = if(isValid) null else "Please enter something"

        return isValid
    }
}

interface OnInputCompleteListener {
    fun onSubmit(input: String)
}