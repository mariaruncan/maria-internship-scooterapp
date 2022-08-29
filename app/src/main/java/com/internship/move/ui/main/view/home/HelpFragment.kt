package com.internship.move.ui.main.view.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentHelpBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class HelpFragment: Fragment(R.layout.fragment_help) {

    private val binding by viewBinding(FragmentHelpBinding::bind)
    private val args by navArgs<HelpFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messageTV.text = args.helpInfo?.message
    }
}