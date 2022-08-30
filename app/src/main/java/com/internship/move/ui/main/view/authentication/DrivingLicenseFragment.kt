package com.internship.move.ui.main.view.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.internship.move.R
import com.internship.move.databinding.FragmentDrivingLicenseBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class DrivingLicenseFragment: Fragment(R.layout.fragment_driving_license) {

    private val binding by viewBinding(FragmentDrivingLicenseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    private fun initToolbar() {
        val customToolbar = binding.toolbar
        customToolbar.titleTV.text = getString(R.string.driving_license_toolbar_title)
        customToolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        customToolbar.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}