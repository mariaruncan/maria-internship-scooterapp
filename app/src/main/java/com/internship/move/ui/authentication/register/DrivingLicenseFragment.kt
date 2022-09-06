package com.internship.move.ui.authentication.register

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
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
                Toast.makeText(requireContext(), getString(R.string.driving_license_fail_message), LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        binding.addLicenseBtn.setOnClickListener {
            takePicture()
        }
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationIconTint(ResourcesCompat.getColor(resources, R.color.text_color_dark, null))
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
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