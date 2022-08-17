package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.internship.move.util.ONBOARDING_NUMBER_OF_PAGES

typealias OnNextButtonClickListener = (currentPageNo: Int) -> Unit

class PagesAdapter(
    val fragment: Fragment,
    private val onNextButtonClickListener: OnNextButtonClickListener
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = ONBOARDING_NUMBER_OF_PAGES

    override fun createFragment(position: Int): Fragment = PageFragment.newInstance(position, onNextButtonClickListener)
}