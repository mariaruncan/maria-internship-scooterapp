package com.internship.move.feature.home

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.model.HelpInfo
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_ONBOARDING
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
                .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, false)
                .apply()

            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.clearAppBtn.setOnClickListener {
            requireActivity().getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE).edit()
                .putBoolean(KEY_HAS_VISITED_ONBOARDING, false)
                .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, false)
                .apply()

            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.helpBtn.setOnClickListener {
            val message = binding.helpMessageTIET.text.toString()
            val helpInfo = HelpInfo(message)
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToHelpFragment(helpInfo))
        }
    }
}