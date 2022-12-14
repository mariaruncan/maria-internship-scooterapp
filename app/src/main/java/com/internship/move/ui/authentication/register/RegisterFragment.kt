package com.internship.move.ui.authentication.register

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.utils.extensions.addClickableText
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickableText()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.getStartedBtn.setIsLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Alerter.create(requireActivity())
                .setText(message)
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToDrivingLicenseFragment())
        }
    }

    private fun initClickableText() {
        binding.termsPrivacyTV.addClickableText(text = getString(R.string.register_terms_and_conditions_text)) {
            Alerter.create(requireActivity())
                .setText(getString(R.string.register_terms_and_conditions_text))
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }

        binding.termsPrivacyTV.addClickableText(text = getString(R.string.register_privacy_policy_text)) {
            Alerter.create(requireActivity())
                .setText(getString(R.string.register_privacy_policy_text))
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
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
            viewModel.register(
                binding.usernameTIET.text.toString(),
                binding.emailTIET.text.toString(),
                binding.passwordTIET.text.toString()
            )
        }
    }

    private fun areFieldsNotEmpty() =
        !binding.emailTIET.text.isNullOrEmpty() and !binding.usernameTIET.text.isNullOrEmpty() and !binding.passwordTIET.text.isNullOrEmpty()
}