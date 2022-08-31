package com.internship.move.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.model.HelpInfo
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.clearAppBtn.setOnClickListener {
            viewModel.clearApp()
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
        }

        binding.helpBtn.setOnClickListener {
            val message = binding.helpMessageTIET.text.toString()
            val helpInfo = HelpInfo(message)
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToHelpFragment(helpInfo))
        }
    }
}