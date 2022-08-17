package com.internship.move.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingBinding
import com.internship.move.util.ONBOARDING_NUMBER_OF_PAGES
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.parcelize.Parcelize

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        val adapter = PagesAdapter(this, ::onNextButtonClickListener)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
    }

    private fun onNextButtonClickListener(currentPageNo: Int) {
        if (currentPageNo < ONBOARDING_NUMBER_OF_PAGES - 1) {
            binding.viewPager.setCurrentItem(currentPageNo + 1, true)
        } else {
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToAuthenticationGraph())
        }
    }
}