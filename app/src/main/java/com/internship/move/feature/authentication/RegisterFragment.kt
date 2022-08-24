package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.extension.addClickableText
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

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
        binding.emailTIET.doOnTextChanged { text, _, _, _ ->
            changeGetStartedButtonState(
                text?.isEmpty() ?: false,
                binding.usernameTIET.text?.isEmpty() ?: false,
                binding.passwordTIET.text?.isEmpty() ?: false
            )
        }

        binding.usernameTIET.doOnTextChanged { text, _, _, _ ->
            changeGetStartedButtonState(
                binding.emailTIET.text?.isEmpty() ?: false,
                text?.isEmpty() ?: false,
                binding.passwordTIET.text?.isEmpty() ?: false
            )
        }

        binding.passwordTIET.doOnTextChanged { text, _, _, _ ->
            changeGetStartedButtonState(
                binding.emailTIET.text?.isEmpty() ?: false,
                binding.usernameTIET.text?.isEmpty() ?: false,
                text?.isEmpty() ?: false
            )
        }

        binding.getStartedBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeGraph())
        }
    }

    private fun changeGetStartedButtonState(emailIsEmpty: Boolean, usernameIsEmpty: Boolean, passwordIsEmpty: Boolean) {
        binding.getStartedBtn.isEnabled = !emailIsEmpty and !usernameIsEmpty and !passwordIsEmpty
    }

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}