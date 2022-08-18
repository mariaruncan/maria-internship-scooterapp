package com.internship.move.feature.onboarding.adapter

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentOnboardingPageBinding
import com.internship.move.feature.onboarding.SkipCallback
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {

    private val binding by viewBinding(FragmentOnboardingPageBinding::bind)
    private var pageDetails: OnboardingPageDetails? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            pageDetails = it.getParcelable(PAGE_DETAILS_KEY)
        }
        displayData()
    }

    private fun displayData() {
        val details = pageDetails ?: return
        ResourcesCompat.getDrawable(resources, details.backgroundId, null)
        binding.backgroundIV.setImageDrawable(ResourcesCompat.getDrawable(resources, details.backgroundId, null))
        binding.titleTV.text = getText(details.titleId)
        binding.descriptionTV.text = getText(details.descriptionId)

        if (details.isLastPage) {
            binding.skipTV.isVisible = false
        } else {
            binding.skipTV.setOnClickListener {
                (parentFragment as SkipCallback).onSkipButtonClick()
            }
        }
    }

    companion object {

        private const val PAGE_DETAILS_KEY = "PAGE_DETAILS"

        fun newInstance(pageDetails: OnboardingPageDetails) = OnboardingPageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PAGE_DETAILS_KEY, pageDetails)
            }
        }
    }
}