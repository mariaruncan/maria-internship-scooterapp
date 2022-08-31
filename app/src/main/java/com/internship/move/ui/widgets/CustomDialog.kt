package com.internship.move.ui.customviews

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.fragment.app.DialogFragment
import com.internship.move.R
import com.internship.move.databinding.DialogCustomBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class CustomDialog : DialogFragment(R.layout.dialog_custom) {

    private val binding by viewBinding(DialogCustomBinding::bind)
    private var firstBtnCallback: View.OnClickListener? = null
    private var secondBtnCallback: View.OnClickListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        isCancelable = false

        arguments?.let {
            binding.titleTV.text = it.getString(TITLE_TEXT)
            binding.descriptionTV.text = it.getString(DESCRIPTION_TEXT)
            binding.firstBtn.text = it.getString(FIRST_BTN_TEXT)
            binding.secondBtn.text = it.getString(SECOND_BTN_TEXT)
        }

        if (firstBtnCallback != null) {
            binding.firstBtn.visibility = VISIBLE
            binding.firstBtn.setOnClickListener(firstBtnCallback)
        }

        if (secondBtnCallback != null) {
            binding.secondBtn.visibility = VISIBLE
            binding.secondBtn.setOnClickListener(secondBtnCallback)
        }
    }

    fun setFirstButtonClickListener(callback: View.OnClickListener) {
        firstBtnCallback = callback
    }

    fun setSecondButtonClickListener(callback: View.OnClickListener) {
        secondBtnCallback = callback
    }

    companion object {
        private const val TITLE_TEXT = "TITLE_TEXT"
        private const val DESCRIPTION_TEXT = "DESCRIPTION"
        private const val FIRST_BTN_TEXT = "FIRST_BTN_TEXT"
        private const val SECOND_BTN_TEXT = "SECOND_BTN_TEXT"

        fun newInstance(title: String, description: String, firstBtnText: String, secondBtnText: String? = null) =
            CustomDialog().apply {
                arguments = Bundle().apply {
                    putString(TITLE_TEXT, title)
                    putString(DESCRIPTION_TEXT, description)
                    putString(FIRST_BTN_TEXT, firstBtnText)
                    putString(SECOND_BTN_TEXT, secondBtnText)
                }
            }
    }
}