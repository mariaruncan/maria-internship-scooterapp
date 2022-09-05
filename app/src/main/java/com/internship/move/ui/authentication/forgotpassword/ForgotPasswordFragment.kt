package com.internship.move.ui.authentication.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentForgotPasswordBinding
import com.internship.move.ui.widgets.CustomDialog
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.emailTIET.doOnTextChanged { text, _, _, _ ->
            binding.sendResetLinkBtn.isEnabled = !text.isNullOrEmpty()
        }

        binding.sendResetLinkBtn.setOnClickListener {
            val dialog = CustomDialog.newInstance(
                getString(R.string.forgot_password_dialog_title),
                getString(R.string.forgot_password_dialog_description),
                getString(R.string.forgot_password_dialog_ok_btn_text)
            )
            dialog.setFirstButtonClickListener {
                dialog.dismiss()
                findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
            }
            dialog.show(parentFragmentManager, FORGOT_PASSWORD_DIALOG_TAG)
        }
    }

    companion object {
        private const val FORGOT_PASSWORD_DIALOG_TAG = "FORGOT_PASSWORD_DIALOG_TAG"
    }
}