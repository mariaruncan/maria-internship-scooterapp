package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.addClickableSpan
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private var emailIsEmpty = true
    private var usernameIsEmpty = true
    private var passwordIsEmpty = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initClickableText()
        initFieldsListeners()
    }

    private fun initFieldsListeners() {
        binding.emailTIET.addTextChangedListener {
            emailIsEmpty = binding.emailTIET.text?.isEmpty() ?: false
            if (!emailIsEmpty) {
                changeGetStartedButtonState()
            }
        }

        binding.usernameTIET.addTextChangedListener {
            usernameIsEmpty = binding.usernameTIET.text?.isEmpty() ?: false
            if (!usernameIsEmpty) {
                changeGetStartedButtonState()
            }
        }

        binding.passwordTIET.addTextChangedListener {
            passwordIsEmpty = binding.emailTIET.text?.isEmpty() ?: false
            if (!passwordIsEmpty) {
                changeGetStartedButtonState()
            }
        }
    }

    private fun changeGetStartedButtonState() {
        binding.getStartedBtn.isEnabled = !emailIsEmpty and !usernameIsEmpty and !passwordIsEmpty
    }

    private fun initClickableText() {
        binding.termsPrivacyTV.addClickableSpan(getString(R.string.register_terms_and_conditions_text)) {
            Toast.makeText(context, getString(R.string.register_terms_and_conditions_text), Toast.LENGTH_SHORT).show()
        }
        binding.termsPrivacyTV.addClickableSpan(getString(R.string.register_privacy_policy_text)) {
            Toast.makeText(context, getString(R.string.register_privacy_policy_text), Toast.LENGTH_SHORT).show()
        }
        binding.loginTV.addClickableSpan(getString(R.string.register_login_here_text)) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    private fun initListeners() {
        binding.getStartedBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeGraph())
        }
    }

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}