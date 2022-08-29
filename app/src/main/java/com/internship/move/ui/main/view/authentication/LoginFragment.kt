package com.internship.move.ui.main.view.authentication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.ui.main.viewmodel.MainViewModel
import com.internship.move.util.Constants
import com.internship.move.util.extension.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by lazy { MainViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickableText()
        initListeners()
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
            binding.loginBtn.setIsLoading(true)
            Handler(Looper.getMainLooper()).postDelayed({
                updateSharedPreferences()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeGraph())
            }, Constants.LOADING_DELAY)
        }
    }

    private fun areFieldsNotEmpty() = !binding.emailTIET.text.isNullOrEmpty() and !binding.passwordTIET.text.isNullOrEmpty()

    private fun updateSharedPreferences() {
        viewModel.login("email", "pass")
    }
}