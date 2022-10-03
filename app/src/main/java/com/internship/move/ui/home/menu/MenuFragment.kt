package com.internship.move.ui.home.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.databinding.FragmentMenuBinding
import com.internship.move.ui.home.MainViewModel
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding(FragmentMenuBinding::bind)
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        binding.titleTV.text = resources.getString(R.string.menu_title_template, viewModel.currentUser.value?.name)
        binding.ridesNumberTV.text = resources.getString(R.string.menu_total_rides_template, viewModel.currentUser.value?.numberOfTrips)
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToMapFragment())
        }

        binding.seeRidesBtn.setOnClickListener {
            // see rides; open new fragment
        }

        binding.accountTV.setOnClickListener {
            // open account settings
        }

        binding.changePassTV.setOnClickListener {
            // change pass
        }

        binding.termsAndConditionsTV.setOnClickListener {
            Alerter.create(requireActivity())
                .setText(getString(R.string.menu_terms_and_conditions_text))
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }

        binding.privacyPolicyTV.setOnClickListener {
            Alerter.create(requireActivity())
                .setText(getString(R.string.menu_privacy_policy_text))
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }

        binding.rateTV.setOnClickListener {
            Alerter.create(requireActivity())
                .setText(getString(R.string.menu_rate_us_text))
                .setTextAppearance(R.style.AlertTextAppearance)
                .setBackgroundColorRes(R.color.primary_color)
                .enableSwipeToDismiss()
                .show()
        }
    }
}