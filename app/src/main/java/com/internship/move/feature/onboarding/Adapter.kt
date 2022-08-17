package com.internship.move.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val NUM_PAGES = 4

typealias OnNextButtonClickListener = (currentPageNo: Int) -> Unit

class PagesAdapter(
    val fragment: Fragment,
    private val onNextButtonClickListener: OnNextButtonClickListener
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = PageFragment.newInstance(position, onNextButtonClickListener)
}