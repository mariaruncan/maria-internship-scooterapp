package com.internship.move.ui.home.unlock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class UnlockFragment : Fragment(R.layout.fragment_unlock) {

    private val binding by viewBinding(FragmentUnlockBinding::bind)
    private val viewModel: MainViewModel by viewModel()
    private val args by navArgs<UnlockFragmentArgs>()
    private var cameraSource: CameraSource? = null
    private var scooterId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        when (args.unlockMethod) {
            UnlockMethod.QR -> initQRUnlock()
            UnlockMethod.NFC -> initNFCUnlock()
            UnlockMethod.PIN -> initPinUnlock()
        }

        binding.closeBtn.setOnClickListener {
            requireActivity().onBackPressed()
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

    private fun initQRScanner() {
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
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
                if (barcodes.size() == 1) {
                    val x = barcodes.valueAt(0).rawValue
                    println(x.toString())
                }
            }

            override fun release() {}
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
        binding.root.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_default, null)
        binding.toolbarTitleTV.text = resources.getString(R.string.unlock_code_toolbar_title)
        binding.titleTV.text = resources.getString(R.string.unlock_code_title)
        binding.descriptionTV.text = resources.getString(R.string.unlock_code_description)
        binding.codeInputLL.isVisible = true
        binding.firstBtn.text = resources.getString(R.string.unlock_qr_btn_text)
        binding.secondBtn.text = resources.getString(R.string.unlock_nfc_btn_text)

        // set TIL listeners
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
            viewModel.scanScooter(UnlockMethod.PIN, scooterId, LatLng(args.latitude.toDouble(), args.longitude.toDouble()))
            requireActivity().onBackPressed()
        }


        binding.firstBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.QR))
        }

        binding.secondBtn.setOnClickListener {
            findNavController().navigate(UnlockFragmentDirections.actionUnlockFragmentSelf(args.longitude, args.latitude, UnlockMethod.NFC))
        }
    }
}

enum class UnlockMethod {
    QR,
    NFC,
    PIN;
}