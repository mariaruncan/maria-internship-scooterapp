package com.internship.move.feature.authentication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.DialogForgotPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ForgotPasswordDialog : DialogFragment(R.layout.dialog_forgot_password) {

    private val binding by viewBinding(DialogForgotPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        binding.okBtn.setOnClickListener {
            dismiss()
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
        }
    }
}