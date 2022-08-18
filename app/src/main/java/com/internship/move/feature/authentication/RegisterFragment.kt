package com.internship.move.feature.authentication

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeBtn.setOnClickListener {
            updateSharedPreferences()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeGraph())
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
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