package com.internship.move.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentHomeBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}