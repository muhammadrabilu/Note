package com.rabilu.note.util

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.isEditTextEmpty() =
    this.editText!!.text.isEmpty()

fun TextInputLayout.getText() =
    this.editText!!.text.toString()


fun isValidPassword(textInputLayout: TextInputLayout) =
    textInputLayout.editText!!.text.isNotEmpty() && textInputLayout.editText!!.text.length > 6

//fun AlertDialog.progressDialog(view: View){
//    val layout = layoutInflater.inflate()
//    this.setView()
//}

