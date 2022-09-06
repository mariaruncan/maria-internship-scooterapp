package com.internship.move.ui.authentication.register

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.internship.move.R
import com.internship.move.databinding.FragmentViewLicenseBinding
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewLicenseFragment : Fragment(R.layout.fragment_view_license) {

    private val binding by viewBinding(FragmentViewLicenseBinding::bind)
    private val viewModel: AuthenticationViewModel by viewModel()
    private val args by navArgs<ViewLicenseFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binding.previewPictureIV.setImageBitmap(BitmapFactory.decodeFile(args.imagePath))
        binding.findScootersBtn.setIsLoading(true)
        binding.findScootersBtn.setOnClickListener {
            findNavController().navigate(ViewLicenseFragmentDirections.actionViewLicenseFragmentToHomeGraph())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.addDrivingLicense(args.imagePath)
            if (response == null) {
                Toast.makeText(requireContext(), "Something went wrong...", Toast.LENGTH_SHORT).show()
                return@launch
            }
            binding.statusTV.text = getString(R.string.driving_license_validated_status)
            binding.previewPictureIV.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_checked, null))
            binding.findScootersBtn.setIsEnabled(true)
        }
    }

    private fun initObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.findScootersBtn.setIsLoading(isLoading)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}