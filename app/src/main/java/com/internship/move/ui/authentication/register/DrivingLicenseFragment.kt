package com.internship.move.ui.authentication.register

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.internship.move.BuildConfig
import com.internship.move.R
import com.internship.move.databinding.FragmentDrivingLicenseBinding
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.File

class DrivingLicenseFragment : Fragment(R.layout.fragment_driving_license) {

    private val binding by viewBinding(FragmentDrivingLicenseBinding::bind)
    private val takeImageLauncher: ActivityResultLauncher<Uri?>
    private var latestLicensePhotoUri: Uri? = null
    private var licenseAbsolutePath: String? = null

    init {
        takeImageLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess && latestLicensePhotoUri != null) {
                findNavController().navigate(
                    DrivingLicenseFragmentDirections.actionDrivingLicenseFragmentToViewLicenseFragment(
                        licenseAbsolutePath.toString()
                    )
                )
            } else {
                Alerter.create(requireActivity())
                    .setText(getString(R.string.driving_license_fail_message))
                    .setTextAppearance(R.style.AlertTextAppearance)
                    .setBackgroundColorRes(R.color.primary_color)
                    .enableSwipeToDismiss()
                    .show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        binding.addLicenseBtn.isEnabled = false
        binding.addLicenseBtn.setOnClickListener {
            takePicture()
        }

        checkCameraPermission()
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationIconTint(ResourcesCompat.getColor(resources, R.color.text_color_dark, null))
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { hasUserAcceptedPermissions ->
                binding.addLicenseBtn.isEnabled = hasUserAcceptedPermissions
                if (!hasUserAcceptedPermissions) {
                    Alerter.create(requireActivity())
                        .setText(getString(R.string.driving_license_permission_denied))
                        .setTextAppearance(R.style.AlertTextAppearance)
                        .setBackgroundColorRes(R.color.primary_color)
                        .enableSwipeToDismiss()
                        .show()
                }
            }.launch(Manifest.permission.CAMERA)
        } else {
            binding.addLicenseBtn.isEnabled = true
        }
    }

    private fun takePicture() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestLicensePhotoUri = uri
                takeImageLauncher.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile(IMAGE_NAME, IMAGE_EXTENSION).apply {
            createNewFile()
            deleteOnExit()
        }
        licenseAbsolutePath = tmpFile.absolutePath
        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    companion object {
        private const val IMAGE_NAME = "tmp_image_file"
        private const val IMAGE_EXTENSION = ".jpeg"
    }
}