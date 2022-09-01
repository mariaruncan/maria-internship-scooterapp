package com.internship.move.ui.authentication.register

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.internship.move.BuildConfig
import com.internship.move.R
import com.internship.move.databinding.FragmentDrivingLicenseBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.File

class DrivingLicenseFragment : Fragment(R.layout.fragment_driving_license) {

    private val binding by viewBinding(FragmentDrivingLicenseBinding::bind)
    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                findNavController().navigate(DrivingLicenseFragmentDirections.actionDrivingLicenseFragmentToViewLicenseFragment(uri.toString()))
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.driving_license_fail_message), Toast.LENGTH_SHORT).show()
        }
    }
    private var latestTmpUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        binding.addLicenseBtn.setOnClickListener {
            takePicture()
        }
    }

    private fun initToolbar() {
        val customToolbar = binding.toolbar
        customToolbar.titleTV.text = getString(R.string.driving_license_toolbar_title)
        customToolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        customToolbar.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun takePicture() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}