package com.internship.move.utils.extensions

import android.text.SpannableString
import android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.TextView.BufferType.SPANNABLE
import com.internship.move.R
import com.internship.move.ui.authentication.OnTextSpanClickCallback

fun TextView.addClickableText(text: String, color: Int = context.getColor(R.color.pink), callback: OnTextSpanClickCallback) {
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {
        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = true
            textPaint.color = color
        }

        override fun onClick(view: View) {
            callback.invoke()
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(text, 0)
    spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + text.length, SPAN_EXCLUSIVE_INCLUSIVE)

    this.movementMethod = LinkMovementMethod.getInstance()
    this.setText(spannableString, SPANNABLE)
}