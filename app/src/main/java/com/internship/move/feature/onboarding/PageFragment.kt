package com.internship.move.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentPageBinding
import com.internship.move.util.ONBOARDING_NUMBER_OF_PAGES
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.Serializable


class PageFragment : Fragment(R.layout.fragment_page) {

    private val binding by viewBinding(FragmentPageBinding::bind)
    private var pageNumber: Int = 0
    private var onNextButtonClickListener: OnNextButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            pageNumber = it.getInt(PAGE_NUMBER_KEY)
            onNextButtonClickListener = it.getSerializable(NEXT_BTN_CLICK_LISTENER_KEY) as OnNextButtonClickListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind()
    }

    private fun bind() {
        binding.backgroundIV.setImageDrawable(getPageBackground())
        if (pageNumber == ONBOARDING_NUMBER_OF_PAGES - 1)
            binding.nextBtn.text = requireContext().getText(R.string.get_started_label_text)
        binding.nextBtn.setOnClickListener {
            onNextButtonClickListener?.let { callbackFunction -> callbackFunction(pageNumber) }
        }
        binding.skipTV.setOnClickListener {
            onNextButtonClickListener?.let { callbackFunction -> callbackFunction(ONBOARDING_NUMBER_OF_PAGES) }
        }
        binding.titleTV.text = getPageTitle()
        binding.descriptionTV.text = getPageDescription()
    }

    private fun getPageBackground() = when (pageNumber) {
        0 -> AppCompatResources.getDrawable(requireContext(), R.drawable.bg_first_onboarding_page)
        1 -> AppCompatResources.getDrawable(requireContext(), R.drawable.bg_second_onboarding_page)
        2 -> AppCompatResources.getDrawable(requireContext(), R.drawable.bg_third_onboarding_page)
        3 -> AppCompatResources.getDrawable(requireContext(), R.drawable.bg_fourth_onboarding_page)
        else -> throw IllegalArgumentException("Wrong page number.")
    }

    private fun getPageTitle(): String = when (pageNumber) {
        0 -> requireContext().getString(R.string.title_first_page)
        1 -> requireContext().getString(R.string.title_second_page)
        2 -> requireContext().getString(R.string.title_third_page)
        3 -> requireContext().getString(R.string.title_fourth_page)
        else -> throw IllegalArgumentException("Wrong page number.")
    }

    private fun getPageDescription(): String = when (pageNumber) {
        0 -> requireContext().getString(R.string.description_first_page)
        1 -> requireContext().getString(R.string.description_second_page)
        2 -> requireContext().getString(R.string.description_third_page)
        3 -> requireContext().getString(R.string.description_fourth_page)
        else -> throw IllegalArgumentException("Wrong page number.")
    }

    companion object {

        private const val PAGE_NUMBER_KEY = "page_number"
        private const val NEXT_BTN_CLICK_LISTENER_KEY = "next_btn_click_listener"

        fun newInstance(pageNr: Int, onNextButtonClickListener: OnNextButtonClickListener) = PageFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE_NUMBER_KEY, pageNr)
                putSerializable(NEXT_BTN_CLICK_LISTENER_KEY, onNextButtonClickListener as Serializable)
            }
        }
    }
}