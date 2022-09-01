package com.internship.move.ui.authentication.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.utils.Constants
import com.internship.move.utils.extensions.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickableText()
        initListeners()
    }

    private fun initClickableText() {
        binding.termsPrivacyTV.addClickableText(text = getString(R.string.register_terms_and_conditions_text)) {
            Toast.makeText(context, getString(R.string.register_terms_and_conditions_text), Toast.LENGTH_SHORT).show()
        }

        binding.termsPrivacyTV.addClickableText(text = getString(R.string.register_privacy_policy_text)) {
            Toast.makeText(context, getString(R.string.register_privacy_policy_text), Toast.LENGTH_SHORT).show()
        }

        binding.loginTV.addClickableText(text = getString(R.string.register_login_here_text)) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun initListeners() {
        binding.emailTIET.doOnTextChanged { _, _, _, _ ->
            binding.getStartedBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.usernameTIET.doOnTextChanged { _, _, _, _ ->
            binding.getStartedBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.passwordTIL.isEndIconVisible = false
        binding.passwordTIET.setOnFocusChangeListener { _, hasFocus ->
            binding.passwordTIL.isEndIconVisible = hasFocus
        }

        binding.passwordTIET.doOnTextChanged { _, _, _, _ ->
            binding.getStartedBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.getStartedBtn.setOnClickListener {
            binding.getStartedBtn.setIsLoading(true)
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.register(
                    binding.emailTIET.text.toString(),
                    binding.usernameTIET.text.toString(),
                    binding.passwordTIET.text.toString()
                )
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToDrivingLicenseFragment())
            }, Constants.LOADING_DELAY)
        }
    }

    private fun areFieldsNotEmpty() =
        !binding.emailTIET.text.isNullOrEmpty() and !binding.usernameTIET.text.isNullOrEmpty() and !binding.passwordTIET.text.isNullOrEmpty()
}