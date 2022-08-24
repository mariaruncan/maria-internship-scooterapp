package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.extension.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

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
        binding.emailTIET.doOnTextChanged { text, _, _, _ ->
            changeLoginButtonState(
                text?.isEmpty() ?: false,
                binding.passwordTIET.text?.isEmpty() ?: false
            )
        }

        binding.passwordTIET.doOnTextChanged { text, _, _, _ ->
            changeLoginButtonState(
                binding.emailTIET.text?.isEmpty() ?: false,
                text?.isEmpty() ?: false
            )
        }

        binding.loginBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeGraph())
        }
    }

    private fun changeLoginButtonState(emailIsEmpty: Boolean, passwordIsEmpty: Boolean) {
        binding.loginBtn.isEnabled = !emailIsEmpty and !passwordIsEmpty
    }

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}