package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
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
import com.internship.move.util.Constants
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
                updateSharedPreferences()
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeGraph())
            }, Constants.LOADING_DELAY)
        }
    }

    private fun areFieldsNotEmpty() =
        !binding.emailTIET.text.isNullOrEmpty() and !binding.usernameTIET.text.isNullOrEmpty() and !binding.passwordTIET.text.isNullOrEmpty()

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}