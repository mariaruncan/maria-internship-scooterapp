package com.internship.move.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.internship.move.R
import com.internship.move.databinding.ViewLoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewLoadingButtonBinding.inflate(LayoutInflater.from(context), this)
    private var btnText: CharSequence = ""

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)) {
            setIsEnabled(getBoolean(R.styleable.LoadingButton_isEnabled, true))
            btnText = getText(R.styleable.LoadingButton_text)
            recycle()
        }

        binding.loadingBtn.text = btnText
    }

    fun setIsEnabled(isEnabled: Boolean) {
        binding.loadingBtn.isEnabled = isEnabled
        if(isEnabled) {
            setIsLoading(false)
        }
    }

    fun setIsLoading(isLoading: Boolean) {
        binding.loadingBtn.isClickable = !isLoading
        binding.loadingPB.isVisible = isLoading
        binding.loadingBtn.text = if (isLoading) "" else btnText
        if (isLoading) {
            setIsEnabled(false)
        }
    }

    override fun setOnClickListener(callback: OnClickListener?) {
        binding.loadingBtn.setOnClickListener(callback)
    }
}