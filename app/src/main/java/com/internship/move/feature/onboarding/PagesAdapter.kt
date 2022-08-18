package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagesAdapter(
    fragment: Fragment,
    private val pagesDetails: List<PageDetails>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pagesDetails.size

    override fun createFragment(position: Int): Fragment = OnboardingPageFragment.newInstance(pagesDetails[position])
}