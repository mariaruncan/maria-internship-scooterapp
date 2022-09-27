package com.internship.move.ui.home.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.data.model.CurrentLocationData
import com.internship.move.data.model.Scooter
import com.internship.move.data.model.UserStatus
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.databinding.ViewStartRideDialogBinding
import com.internship.move.databinding.ViewUnlockDialogBinding
import com.internship.move.ui.home.MainViewModel
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.extensions.getDrawableToBitmapDescriptor
import com.internship.move.utils.extensions.setBatteryIcon
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MainViewModel by sharedViewModel()

    private var locationGranted: Boolean = false
    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(requireActivity()) }
    private val geocoder: Geocoder by lazy { Geocoder(requireContext()) }
    private var map: GoogleMap? = null
    private var clusterManager: ClusterManager<Scooter>? = null
    private var selectedMarker: Marker? = null
    private val currentLocationData = CurrentLocationData()

    private val onClusterItemClickListener by lazy { initOnClusterItemClickListener() }
    private val onClusterClickListener by lazy { initOnClusterClickListener() }
    private val onMapReadyCallback by lazy { initOnMapReadyCallback() }
    private val onMapClickListener by lazy { initOnMapClickListener() }
    private val locationCallback by lazy { initLocationCallback() }

    private var scootersJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                viewModel.clearApp()
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
            }
            else {
                when(user.status){
                    UserStatus.FREE -> {
                        if (viewModel.status == UserStatus.FREE) {
                            startScootersUpdates()
                            displayCurrentLocation()
                        }
                    }

                    UserStatus.SCANNED -> {
                        stopScootersUpdates()
                        showStartRideDialog()
                    }
                    UserStatus.BUSY -> {
                        showCurrentRideDialog()
                    }
                }
            }
        }

        checkLocationPermissions(savedInstanceState)
    }

    private fun startScootersUpdates() {
        stopScootersUpdates()
        scootersJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                viewModel.getAllScooters()
                delay(60000)
            }
        }
    }

    private fun stopScootersUpdates() {
        scootersJob?.cancel()
        scootersJob = null
    }

    private fun checkLocationPermissions(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                locationGranted = result
                if (!locationGranted) {
                    Toast.makeText(requireContext(), resources.getString(R.string.map_location_denied_message), Toast.LENGTH_SHORT).show()
                }
                initMap(savedInstanceState)
                initToolbar()
            }.launch(ACCESS_FINE_LOCATION)
        } else {
            locationGranted = true
            initMap(savedInstanceState)
            initToolbar()
        }
    }

    private fun initToolbar() {
        binding.menuBtn.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionMapFragmentToMenuFragment())
        }

        map?.uiSettings?.isMapToolbarEnabled = false

        if (!locationGranted) {
            binding.locationBtn.setImageResource(R.drawable.ic_no_location)
        }

        binding.locationBtn.setOnClickListener {
            if (locationGranted) {
                if (currentLocationData.location != null) {
                    map?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(currentLocationData.location!!.latitude, currentLocationData.location!!.longitude),
                            ZOOM_VALUE
                        )
                    )
                }
            } else {
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, ZOOM_VALUE))
            }
        }
    }

    override fun onDestroyView() {
        map?.clear()
        map = null
        fusedLocationClient.removeLocationUpdates(locationCallback)
        stopScootersUpdates()

        super.onDestroyView()
    }

    private fun initMap(savedInstanceState: Bundle?) {
        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
            binding.map.onCreate(savedInstanceState)
            binding.map.onResume()
            binding.map.getMapAsync(onMapReadyCallback)
        }
    }

    private fun initOnMapReadyCallback() = OnMapReadyCallback { googleMap ->
        map = googleMap
        map!!.setOnMarkerClickListener { marker ->
            map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, ZOOM_VALUE))
            true
        }
        try {
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json)
            )
        } catch (e: Resources.NotFoundException) {
            Log.d("", e.message, e)
        }

        if (locationGranted) {
            map!!.setOnMapClickListener(onMapClickListener)
            initClustering()
            initObservers()
        }
        displayCurrentLocation()
    }

    private fun initOnMapClickListener() = GoogleMap.OnMapClickListener {
        if (selectedMarker != null) {
            try {
                selectedMarker?.setIcon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_scooter))
            } catch (ex: IllegalArgumentException) {
                Log.d("", ex.message, ex)
            }
            hideInfoWindow()
        }
        selectedMarker = null
    }

    private fun initClustering() {
        clusterManager = ClusterManager<Scooter>(requireContext(), map)
        clusterManager!!.setOnClusterClickListener(onClusterClickListener)
        clusterManager!!.setOnClusterItemClickListener(onClusterItemClickListener)
        clusterManager!!.renderer = map?.let { ScooterRenderer(requireContext(), it, clusterManager!!) }

        map?.setOnCameraIdleListener {
            clusterManager!!.onCameraIdle()
        }
    }

    private fun initOnClusterClickListener() = OnClusterClickListener<Scooter> { cluster ->
        if (cluster != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.position, ZOOM_VALUE_CLUSTER))
        }
        true
    }

    private fun initOnClusterItemClickListener() = OnClusterItemClickListener<Scooter> { item ->
        if (selectedMarker != null) {
            selectedMarker?.setIcon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_scooter))
        }

        selectedMarker = (clusterManager?.renderer as DefaultClusterRenderer<Scooter>).getMarker(item)
        if (selectedMarker != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker!!.position, ZOOM_VALUE))
            selectedMarker?.setIcon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_scooter_selected))

            if (item != null) {
                displayInfoWindow(item)
            }
        }
        true
    }

    private fun initObservers() {
        viewModel.scootersList.observe(viewLifecycleOwner) { scootersList ->
            displayScooters(scootersList)
        }
    }

    private fun displayScooters(scooters: List<Scooter>) {
        clusterManager?.clearItems()
        clusterManager?.addItems(scooters)
        clusterManager?.cluster()
    }

    @SuppressLint("MissingPermission")
    private fun displayCurrentLocation() {
        if (!locationGranted) {
            map?.addMarker(
                MarkerOptions().position(DEFAULT_LOCATION)
                    .icon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_default_location))
                    .anchor(.5f, .5f)
            )
            map?.addCircle(
                CircleOptions().center(DEFAULT_LOCATION).radius(CIRCLE_RADIUS_DEFAULT)
                    .fillColor(ColorUtils.setAlphaComponent(resources.getColor(R.color.indigo, null), CIRCLE_ALPHA))
                    .strokeWidth(0f)
            )

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, ZOOM_VALUE))
            binding.titleTV.text = resources.getString(R.string.map_toolbar_default_title)
        } else {
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
        }
    }

    private fun initLocationCallback() = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation ?: return
            val position = LatLng(location.latitude, location.longitude)

            if (currentLocationData.location == null) {
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_VALUE))
            }

            currentLocationData.marker?.remove()
            currentLocationData.circle?.remove()

            binding.titleTV.text = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()?.locality
            currentLocationData.marker = map?.addMarker(
                MarkerOptions().position(position).icon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_current_location))
                    .anchor(.5f, .5f)
            )

            currentLocationData.circle = map?.addCircle(
                CircleOptions().center(position).radius(CIRCLE_RADIUS)
                    .fillColor(ColorUtils.setAlphaComponent(resources.getColor(R.color.indigo, null), CIRCLE_ALPHA))
                    .strokeWidth(0f)
            )

            currentLocationData.location = location
        }
    }


    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = 10000
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    private fun displayInfoWindow(scooter: Scooter) {
        val infoWindow = binding.scooterInfoWindow
        infoWindow.root.isVisible = true
        infoWindow.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        infoWindow.batteryIV.setBatteryIcon(scooter.batteryLevel)

        infoWindow.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        val address = geocoder.getFromLocation(scooter.latLng.latitude, scooter.latLng.longitude, 1)?.firstOrNull() ?: return
        infoWindow.addressTV.text = SCOOTER_ADDRESS_TEMPLATE.format(address.thoroughfare, address.subThoroughfare)

        infoWindow.unlockBtn.setOnClickListener {
            showUnlockDialog(scooter)
            hideInfoWindow()
        }
    }

    private fun hideInfoWindow() {
        binding.scooterInfoWindow.unlockBtn.setOnClickListener { }
        binding.scooterInfoWindow.root.isVisible = false
    }

    private fun showUnlockDialog(scooter: Scooter) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        bottomSheetDialog.setOnDismissListener {
            selectedMarker?.setIcon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_scooter))
        }

        val dialogBinding = ViewUnlockDialogBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        dialogBinding.nfcBtn.setOnClickListener {
            bottomSheetDialog.hide()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: 0f,
                    currentLocationData.location?.latitude?.toFloat() ?: 0f,
                    UnlockMethod.NFC
                )
            )
        }

        dialogBinding.qrBtn.setOnClickListener {
            bottomSheetDialog.hide()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: 0f,
                    currentLocationData.location?.latitude?.toFloat() ?: 0f,
                    UnlockMethod.QR
                )
            )
        }

        dialogBinding.codeBtn.setOnClickListener {
            bottomSheetDialog.hide()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: 0f,
                    currentLocationData.location?.latitude?.toFloat() ?: 0f,
                    UnlockMethod.PIN
                )
            )
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showStartRideDialog() {
        val scooter: Scooter = viewModel.currentUser.value?.scooter ?: return

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        bottomSheetDialog.setOnDismissListener {
            viewModel.cancelScanScooter()
            startScootersUpdates()
        }

        val dialogBinding = ViewStartRideDialogBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        dialogBinding.startRideBtn.setOnClickListener {
            viewModel.startRide(
                scooter.number,
                currentLocationData.location?.latitude ?: .0,
                currentLocationData.location?.longitude ?: .0
            )
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showCurrentRideDialog() {

    }

    companion object {
        private const val SCOOTER_NUMBER_TEMPLATE = "#%d"
        private const val SCOOTER_BATTERY_TEMPLATE = "%d%%"
        private const val SCOOTER_ADDRESS_TEMPLATE = "%s %s"

        private const val ZOOM_VALUE = 17F
        private const val ZOOM_VALUE_CLUSTER = 14F
        private const val CIRCLE_ALPHA = 26
        private const val CIRCLE_RADIUS = 100.0
        private const val CIRCLE_RADIUS_DEFAULT = 200.0

        private val DEFAULT_LOCATION = LatLng(46.769441, 23.589922)
    }
}
