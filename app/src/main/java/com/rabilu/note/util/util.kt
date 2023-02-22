package com.rabilu.note.util

import android.annotation.SuppressLint
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

fun TextInputLayout.isEditTextEmpty() =
    this.editText!!.text.isEmpty()

fun TextInputLayout.getText() =
    this.editText!!.text.toString()


fun isValidPassword(textInputLayout: TextInputLayout) =
    textInputLayout.editText!!.text.isNotEmpty() && textInputLayout.editText!!.text.length > 6

@SuppressLint("SimpleDateFormat")
fun getDateTime(timeDate: Long): String {

    val cal1: Calendar = Calendar.getInstance()
    cal1.timeInMillis = timeDate
    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy hh:mm",
    )
    return dateFormat.format(cal1.time)
}

//fun AlertDialog.progressDialog(view: View){
//    val layout = layoutInflater.inflate()
//    this.setView()
//}

