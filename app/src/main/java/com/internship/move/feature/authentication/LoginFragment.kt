package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERECES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapGraph())
        }

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun updateSharedPreferences() {
        requireActivity().getSharedPreferences(KEY_APP_PREFERECES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}