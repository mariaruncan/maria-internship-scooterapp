package com.internship.move.feature.authentication

import android.os.Bundle
import android.text.Editable
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
        binding.newPassTIET.doOnTextChanged { text, _, _, _ ->
            binding.resetPassBtn.isEnabled = (text?.isNotEmpty() ?: true) and
                    (binding.confirmPassTIET.text?.isNotEmpty() ?: true)
        }

        binding.confirmPassTIET.doOnTextChanged { text, _, _, _ ->
            binding.confirmPassTIL.isErrorEnabled = false
            binding.resetPassBtn.isEnabled = (text?.isNotEmpty() ?: true) and
                    (binding.confirmPassTIET.text?.isNotEmpty() ?: true)
        }

        binding.resetPassBtn.setOnClickListener {
            if(binding.newPassTIET.text.toString() == binding.confirmPassTIET.text.toString()) {
                findNavController().navigate(ResetPasswordFragmentDirections.actionResetPasswordFragmentToLoginFragment())
            }
            else {
                binding.confirmPassTIL.isErrorEnabled = true
                binding.confirmPassTIL.error = requireContext().getString(R.string.reset_password_do_not_match)
            }
        }
    }
}