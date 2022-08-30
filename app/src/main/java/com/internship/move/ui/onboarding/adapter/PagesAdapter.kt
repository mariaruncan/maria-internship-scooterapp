package com.internship.move.ui.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagesAdapter(
    fragment: Fragment,
    private val pagesDetails: List<OnboardingPageDetails>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pagesDetails.size

    override fun createFragment(position: Int): Fragment = OnboardingPageFragment.newInstance(pagesDetails[position])
}