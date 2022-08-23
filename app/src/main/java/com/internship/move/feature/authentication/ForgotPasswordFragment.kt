package com.internship.move.feature.authentication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentForgotPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonsListeners()
        initFieldsListeners()
    }

    private fun initFieldsListeners() {
        binding.emailTIET.addTextChangedListener {
            binding.sendResetLinkBtn.isEnabled = binding.emailTIET.text?.isNotEmpty() ?: true
        }
    }

    private fun initButtonsListeners() {
        binding.sendResetLinkBtn.setOnClickListener {
            Toast.makeText(context, "Reset link sent!!", Toast.LENGTH_SHORT).show()
        }
    }
}