package com.internship.move.feature.authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentForgotPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.emailTIET.doOnTextChanged { text, _, _, _ ->
            binding.sendResetLinkBtn.isEnabled = text?.isNotEmpty() ?: true
        }

        binding.sendResetLinkBtn.setOnClickListener {
            Toast.makeText(context, "Reset link sent!!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment())
        }
    }
}