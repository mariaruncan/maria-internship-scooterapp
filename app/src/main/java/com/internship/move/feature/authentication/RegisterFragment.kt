package com.internship.move.feature.authentication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentRegisterBinding
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
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
        requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, true)
            .apply()
    }
}