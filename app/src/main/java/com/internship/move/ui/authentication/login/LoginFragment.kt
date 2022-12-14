package com.internship.move.ui.authentication.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.utils.extensions.addClickableText
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: AuthenticationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickableText()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Alerter.create(requireActivity())
                .setText(message)
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loginBtn.setIsLoading(isLoading)
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user.hasDrivingLicense) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeGraph())
            } else {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDrivingLicenseFragment())
            }
        }
    }

    private fun initClickableText() {
        binding.forgotPasswordTV.addClickableText(
            text = getString(R.string.login_forgot_password_text),
            color = requireContext().getColor(R.color.white)
        ) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        binding.registerTV.addClickableText(text = getString(R.string.login_create_account_text)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun initListeners() {
        binding.emailTIET.doOnTextChanged { _, _, _, _ ->
            binding.loginBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.passwordTIL.isEndIconVisible = false
        binding.passwordTIET.setOnFocusChangeListener { _, hasFocus ->
            binding.passwordTIL.isEndIconVisible = hasFocus
        }

        binding.passwordTIET.doOnTextChanged { _, _, _, _ ->
            binding.loginBtn.setIsEnabled(areFieldsNotEmpty())
        }

        binding.loginBtn.setOnClickListener {
            viewModel.login(
                binding.emailTIET.text.toString(),
                binding.passwordTIET.text.toString()
            )
        }
    }

    private fun areFieldsNotEmpty() = !binding.emailTIET.text.isNullOrEmpty() and !binding.passwordTIET.text.isNullOrEmpty()
}