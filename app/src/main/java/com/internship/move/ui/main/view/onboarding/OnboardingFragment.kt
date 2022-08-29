package com.internship.move.ui.main.view.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingBinding
import com.internship.move.ui.main.view.onboarding.adapter.OnboardingPageDetails
import com.internship.move.ui.main.view.onboarding.adapter.PagesAdapter
import com.internship.move.ui.main.viewmodel.MainViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingFragment : Fragment(R.layout.fragment_onboarding), SkipCallback {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val adapter by lazy { PagesAdapter(this, getPagesDetails()) }
    private val viewModel by lazy { MainViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    private fun initViewPager() {
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.dotsIndicator.attachTo(binding.viewPager)

        binding.nextBtn.setOnClickListener {
            if (binding.viewPager.currentItem == adapter.itemCount - 1) {
                updateSharedPreferences()
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToAuthenticationGraph())
            } else {
                binding.viewPager.currentItem += 1
                if (binding.viewPager.currentItem == adapter.itemCount - 1) {
                    binding.nextBtn.text = getText(R.string.onboarding_get_started_btn_text)
                }
            }
        }
    }

    override fun onSkipButtonClick() {
        updateSharedPreferences()
        findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToAuthenticationGraph())
    }

    private fun updateSharedPreferences() {
        viewModel.notifyHasViewedOnboarding()
    }

    private fun getPagesDetails(): List<OnboardingPageDetails> = listOf(
        OnboardingPageDetails(
            R.string.onboarding_title_first_page,
            R.string.onboarding_description_first_page,
            R.drawable.bg_first_onboarding_page
        ),
        OnboardingPageDetails(
            R.string.onboarding_title_second_page,
            R.string.onboarding_description_second_page,
            R.drawable.bg_second_onboarding_page
        ),
        OnboardingPageDetails(
            R.string.onboarding_title_third_page,
            R.string.onboarding_description_third_page,
            R.drawable.bg_third_onboarding_page
        ),
        OnboardingPageDetails(
            R.string.onboarding_title_fourth_page,
            R.string.onboarding_description_fourth_page,
            R.drawable.bg_fourth_onboarding_page,
            true
        )
    )
}