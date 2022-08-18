package com.internship.move.feature.authentication

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentLoginBinding
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
        val sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.has_visited_authentication_key), true)
            apply()
        }
    }
}