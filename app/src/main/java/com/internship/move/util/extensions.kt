package com.internship.move.util

import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.internship.move.R

fun TextView.addClickableText(text: String, callback: View.OnClickListener) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1

    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = true
            textPaint.color = context.getColor(R.color.pink)
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            callback.onClick(view)
        }
    }
    startIndexOfLink = this.text.toString().indexOf(text, startIndexOfLink + 1)
    spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + text.length, SPAN_EXCLUSIVE_EXCLUSIVE)


    this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}