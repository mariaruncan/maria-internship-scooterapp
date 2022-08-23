package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private var emailIsEmpty = true
    private var passwordIsEmpty = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickableText()
        initButtonsListeners()
        initFieldsListeners()
    }

    private fun initClickableText() {
        binding.forgotPasswordTV.addClickableText(getString(R.string.login_forgot_password_text)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }

        binding.registerTV.addClickableText(getString(R.string.login_create_account_text)) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun initFieldsListeners() {
        binding.emailTIET.addTextChangedListener {
            emailIsEmpty = binding.emailTIET.text?.isEmpty() ?: false
            changeLoginButtonState()
        }

        binding.passwordTIET.addTextChangedListener {
            passwordIsEmpty = binding.emailTIET.text?.isEmpty() ?: false
            changeLoginButtonState()
        }
    }

    private fun changeLoginButtonState() {
        binding.loginBtn.isEnabled = !emailIsEmpty and !passwordIsEmpty
    }

    private fun initButtonsListeners() {
        binding.loginBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeGraph())
        }
    }

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}