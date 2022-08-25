package com.internship.move.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.internship.move.R
import com.internship.move.databinding.ViewLoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewLoadingButtonBinding.inflate(LayoutInflater.from(context), this)
    private var btnText: CharSequence = ""
    var isLoading: Boolean = false
        set(value) {
            field = value
            if (value) {
                binding.loadingPB.visibility = View.VISIBLE
                binding.loadingBtn.text = ""
                binding.loadingBtn.isClickable = false
            } else {
                binding.loadingPB.visibility = View.INVISIBLE
                binding.loadingBtn.text = btnText
                binding.loadingBtn.isClickable = true
            }
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
        binding.loadingBtn.text = a.getText(R.styleable.LoadingButton_text)
        setIsEnabled(a.getBoolean(R.styleable.LoadingButton_isEnabled, true))
        btnText = a.getText(R.styleable.LoadingButton_text)
        a.recycle()

        binding.loadingBtn.text = btnText
    }

    fun setIsEnabled(value: Boolean) {
        binding.loadingBtn.isEnabled = value
        if(!value) {
            isLoading = false
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.loadingBtn.setOnClickListener(l)
    }
}