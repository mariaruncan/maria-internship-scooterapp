package com.internship.move.ui.authentication.register

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentViewLicenseBinding
import com.internship.move.utils.Constants
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding

class ViewLicenseFragment : Fragment(R.layout.fragment_view_license) {

    private val binding by viewBinding(FragmentViewLicenseBinding::bind)
    private val args by navArgs<ViewLicenseFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.previewPictureIV.setImageURI(Uri.parse(args.imageUri))
        binding.findScootersBtn.setIsLoading(true)
        binding.findScootersBtn.setOnClickListener {
            findNavController().navigate(ViewLicenseFragmentDirections.actionViewLicenseFragmentToHomeGraph())
        }

        Handler(Looper.getMainLooper()).postDelayed({
            binding.statusTV.text = getString(R.string.driving_license_validated_status)
            binding.previewPictureIV.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_checked, null))
            binding.findScootersBtn.setIsEnabled(true)
        }, Constants.LOADING_DELAY)
    }
}