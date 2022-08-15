package com.internship.move.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentHomeBinding
import com.internship.move.feature.model.HelpInfo
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSplashGraph())
        }

        binding.helpBtn.setOnClickListener {
            val message = binding.helpMessageTIET.text.toString()
            val helpInfo = HelpInfo(message)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHelpFragment(helpInfo))
        }
    }
}