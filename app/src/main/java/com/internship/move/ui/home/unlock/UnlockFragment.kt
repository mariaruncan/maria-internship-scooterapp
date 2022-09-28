package com.internship.move.ui.home.unlock

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.internship.move.R
import com.internship.move.databinding.FragmentUnlockBinding
import com.internship.move.ui.home.MainViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UnlockFragment : Fragment(R.layout.fragment_unlock) {

    private val binding by viewBinding(FragmentUnlockBinding::bind)
    private val viewModel: MainViewModel by sharedViewModel()
    private val args by navArgs<UnlockFragmentArgs>()
    private var cameraSource: CameraSource? = null
    private var scooterId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        when (args.unlockMethod) {
            UnlockMethod.QR -> checkCameraPermission()
            UnlockMethod.NFC -> initNFCUnlock()
            UnlockMethod.PIN -> initPinUnlock()
        }

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentToMapFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.stop()
    }

    private fun initQRUnlock() {
        binding.toolbarTitleTV.text = resources.getString(R.string.unlock_qr_toolbar_title)
        binding.unlockBgIV.setImageResource(R.drawable.bg_unlock_qr)
        binding.cameraSurface.isVisible = true
        binding.titleTV.text = resources.getString(R.string.unlock_qr_title)
        binding.descriptionTV.text = resources.getString(R.string.unlock_qr_description)

        initQRScanner()

        binding.firstBtn.text = resources.getString(R.string.unlock_code_btn_text)
        binding.secondBtn.text = resources.getString(R.string.unlock_nfc_btn_text)

        binding.firstBtn.setOnClickListener {
            findNavController().navigate(
                UnlockFragmentDirections.actionUnlockFragmentSelf(
                    args.longitude,
                    args.latitude,
                    UnlockMethod.PIN
                )
            )
        }

        binding.secondBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.NFC))
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    initQRUnlock()
                } else {
                    Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_LONG).show()
                }
            }.launch(Manifest.permission.CAMERA)
        } else {
            initQRUnlock()
        }
    }

    private fun initQRScanner() {
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(CAMERA_PREVIEW_HEIGHT, CAMERA_PREVIEW_WIDTH)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurface.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource?.start(holder)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                try {
                    cameraSource?.start(holder)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource?.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == BARCODE_MINIMUM_SIZE) {
                    Log.d("BARCODES_VALUE", barcodes.valueAt(0).rawValue)
                }
            }

            override fun release() = Unit
        })
    }

    private fun initNFCUnlock() {
        binding.root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_default, null)
        binding.toolbarTitleTV.text = resources.getString(R.string.unlock_nfc_toolbar_title)
        binding.titleTV.text = resources.getString(R.string.unlock_nfc_title)
        binding.descriptionTV.text = resources.getString(R.string.unlock_nfc_description)
        binding.unlockBgIV.setImageResource(R.drawable.bg_unlock_nfc)
        binding.firstBtn.text = resources.getString(R.string.unlock_qr_btn_text)
        binding.secondBtn.text = resources.getString(R.string.unlock_code_btn_text)

        // listener for nfc

        binding.firstBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.QR))
        }

        binding.secondBtn.setOnClickListener {
            findNavController().navigate(
                UnlockFragmentDirections.actionUnlockFragmentSelf(
                    args.longitude,
                    args.latitude,
                    UnlockMethod.PIN
                )
            )
        }
    }

    private fun initPinUnlock() {
        viewModel.unlockSuccessful.observe(viewLifecycleOwner) {
            if (it == true) {
                viewLifecycleOwner.lifecycleScope.launch {
                    displayUnlockSuccessfulScreen()
                    delay(2000)
                    viewModel.unlockSuccessful.value = false
                    findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentToMapFragment())
                }
            }
        }

        binding.root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_default, null)
        binding.toolbarTitleTV.text = resources.getString(R.string.unlock_code_toolbar_title)
        binding.titleTV.text = resources.getString(R.string.unlock_code_title)
        binding.descriptionTV.text = resources.getString(R.string.unlock_code_description)
        binding.codeInputLL.isVisible = true
        binding.firstBtn.text = resources.getString(R.string.unlock_qr_btn_text)
        binding.secondBtn.text = resources.getString(R.string.unlock_nfc_btn_text)

        binding.firstDigitTIET.doOnTextChanged { text, _, _, _ ->
            binding.firstDigitTIET.clearFocus()
            scooterId = text.toString().toInt()
            binding.secondDigitTIET.requestFocus()
        }

        binding.secondDigitTIET.doOnTextChanged { text, _, _, _ ->
            binding.secondDigitTIET.clearFocus()
            scooterId *= 10
            scooterId += text.toString().toInt()
            binding.thirdDigitTIET.requestFocus()
        }

        binding.thirdDigitTIET.doOnTextChanged { text, _, _, _ ->
            binding.thirdDigitTIET.clearFocus()
            scooterId *= 10
            scooterId += text.toString().toInt()
            binding.fourthDigitTIET.requestFocus()
        }

        binding.fourthDigitTIET.doOnTextChanged { text, _, _, _ ->
            binding.fourthDigitTIET.clearFocus()
            scooterId *= 10
            scooterId += text.toString().toInt()

            binding.progressBar.isVisible = true
            viewModel.scanScooter(UnlockMethod.PIN, scooterId, LatLng(args.latitude.toDouble(), args.longitude.toDouble()))
            findNavController().navigateUp()
        }

        binding.firstBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.QR))
        }

        binding.secondBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.NFC))
        }
    }

    private fun displayUnlockSuccessfulScreen() {
        binding.progressBar.isVisible = false
        binding.closeBtn.isVisible = false
        binding.toolbarTitleTV.isVisible = false
        binding.titleTV.text = resources.getString(R.string.unlock_successful_title)
        binding.descriptionTV.isVisible = false
        binding.codeInputLL.isVisible = false
        binding.unlockBgIV.setImageResource(R.drawable.bg_unlock_successful)
        binding.unlockSuccessfulTV.isVisible = true
        binding.buttonsTV.isVisible = false
        binding.orTV.isVisible = false
        binding.firstBtn.isVisible = false
        binding.secondBtn.isVisible = false
    }

    companion object {
        private const val BARCODE_MINIMUM_SIZE = 1

        private const val CAMERA_PREVIEW_HEIGHT = 1920
        private const val CAMERA_PREVIEW_WIDTH = 1080
    }
}