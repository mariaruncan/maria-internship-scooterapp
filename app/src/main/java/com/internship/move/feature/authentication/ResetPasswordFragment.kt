package com.internship.move.feature.authentication

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentResetPasswordBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val binding by viewBinding(FragmentResetPasswordBinding::bind)
    private val args by navArgs<ResetPasswordFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tokenTV.text = args.token
        initListeners()
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener{
            requireActivity().onBackPressed()
        }

        binding.newPassTIL.isEndIconVisible = false
        binding.newPassTIET.setOnFocusChangeListener { _, hasFocus ->
            binding.newPassTIL.isEndIconVisible = hasFocus
        }

        binding.newPassTIET.doOnTextChanged { _, _, _, _ ->
            binding.resetPassBtn.isEnabled = areFieldsNotEmpty()
        }

        binding.confirmPassTIL.isEndIconVisible = false
        binding.confirmPassTIET.setOnFocusChangeListener { _, hasFocus ->
            binding.confirmPassTIL.isEndIconVisible = hasFocus
        }

        binding.confirmPassTIET.doOnTextChanged { _, _, _, _ ->
            binding.confirmPassTIL.isErrorEnabled = false
            binding.resetPassBtn.isEnabled = areFieldsNotEmpty()
        }

        binding.resetPassBtn.setOnClickListener {
            if (binding.newPassTIET.text.contentEquals(binding.confirmPassTIET.text)) {
                findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment())
            } else {
                binding.confirmPassTIL.isErrorEnabled = true
                binding.confirmPassTIL.error = requireContext().getString(R.string.reset_password_do_not_match)
            }
        }
    }

    private fun areFieldsNotEmpty() = !binding.newPassTIET.text.isNullOrEmpty() and !binding.confirmPassTIET.text.isNullOrEmpty()
}