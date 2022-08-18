package com.internship.move.feature.map

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.feature.model.HelpInfo
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            with(requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit()) {
                putBoolean(getString(R.string.has_visited_authentication_key), false)
                apply()
            }
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.clearAppBtn.setOnClickListener {
            with(requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).edit()) {
                putBoolean(getString(R.string.has_visited_onboarding_key), false)
                putBoolean(getString(R.string.has_visited_authentication_key), false)
                apply()
            }
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.helpBtn.setOnClickListener {
            val message = binding.helpMessageTIET.text.toString()
            val helpInfo = HelpInfo(message)
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToHelpFragment(helpInfo))
        }
    }
}