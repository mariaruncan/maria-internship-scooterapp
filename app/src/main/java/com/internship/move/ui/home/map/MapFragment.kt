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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.data.model.CurrentLocationData
import com.internship.move.data.model.Scooter
import com.internship.move.data.model.UserStatus.BUSY
import com.internship.move.data.model.UserStatus.FREE
import com.internship.move.data.model.UserStatus.SCANNED
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.databinding.ViewStartRideDialogBinding
import com.internship.move.databinding.ViewTripDetailsCollapsedBinding
import com.internship.move.databinding.ViewTripDetailsExpandedBinding
import com.internship.move.databinding.ViewUnlockDialogBinding
import com.internship.move.ui.home.ScooterViewModel
import com.internship.move.ui.home.TripViewModel
import com.internship.move.ui.home.UserViewModel
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.extensions.getDrawableToBitmapDescriptor
import com.internship.move.utils.extensions.setBatteryIcon
import com.tapadoo.alerter.Alerter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val userViewModel: UserViewModel by koinNavGraphViewModel(R.id.home_graph)
    private val scooterViewModel: ScooterViewModel by koinNavGraphViewModel(R.id.home_graph)
    private val tripViewModel: TripViewModel by koinNavGraphViewModel(R.id.home_graph)

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

    private var isLockable: Boolean = true
    private var scootersJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getCurrentUser()

        userViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            createAlerter(message)
        }

        scooterViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            createAlerter(message)
        }

        tripViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            createAlerter(message)
        }

        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                userViewModel.clearApp()
                findNavController().navigate(MapFragmentDirections.actionMapFragmentToSplashGraph())
            } else {
                when (user.status) {
                    FREE -> {
                        startScootersUpdates()
                        displayCurrentLocation()
                    }

                    SCANNED -> {
                        stopScootersUpdates()
                        showStartRideDialog()
                    }
                    BUSY -> {
                        stopScootersUpdates()
                        showCurrentRideDialogCollapsed()
                    }
                }
            }
        }

        checkLocationPermissions(savedInstanceState)
    }

    override fun onDestroyView() {
        map?.clear()
        map = null
        fusedLocationClient.removeLocationUpdates(locationCallback)
        stopScootersUpdates()

        super.onDestroyView()
    }

    private fun startScootersUpdates() {
        stopScootersUpdates()
        scootersJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                scooterViewModel.getAllScooters()
                delay(SCOOTER_UPDATES_INTERVAL)
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
                    createAlerter(getString(R.string.map_location_denied_message))
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
        scooterViewModel.scootersList.observe(viewLifecycleOwner) { scootersList ->
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
                    .anchor(MARKER_ANCHOR, MARKER_ANCHOR)
            )
            map?.addCircle(
                CircleOptions().center(DEFAULT_LOCATION).radius(CIRCLE_RADIUS_DEFAULT)
                    .fillColor(ColorUtils.setAlphaComponent(resources.getColor(R.color.indigo, null), CIRCLE_ALPHA))
                    .strokeWidth(CIRCLE_STROKE_WIDTH)
            )

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, ZOOM_VALUE))
            binding.titleTV.text = resources.getString(R.string.map_toolbar_default_title)
        } else {
            fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
        }
    }

    private fun initLocationCallback() = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val map = this@MapFragment.map ?: return

            val location = locationResult.lastLocation ?: return
            val position = LatLng(location.latitude, location.longitude)

            if (currentLocationData.location == null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_VALUE))
            }

            currentLocationData.marker?.remove()
            currentLocationData.circle?.remove()

            binding.titleTV.text = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()?.locality
            currentLocationData.marker = map.addMarker(
                MarkerOptions().position(position).icon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_current_location))
                    .anchor(MARKER_ANCHOR, MARKER_ANCHOR)
            )

            currentLocationData.circle = map.addCircle(
                CircleOptions().center(position).radius(CIRCLE_RADIUS)
                    .fillColor(ColorUtils.setAlphaComponent(resources.getColor(R.color.indigo, null), CIRCLE_ALPHA))
                    .strokeWidth(CIRCLE_STROKE_WIDTH)
            )

            currentLocationData.location = location
        }
    }


    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = LOCATION_UPDATES_INTERVAL
        priority = PRIORITY_HIGH_ACCURACY
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

        infoWindow.ringBtn.setOnClickListener {
            scooterViewModel.ringScooter(scooter.id)
        }
    }

    private fun hideInfoWindow() {
        binding.scooterInfoWindow.unlockBtn.setOnClickListener { }
        binding.scooterInfoWindow.root.isVisible = false
    }

    private fun showUnlockDialog(scooter: Scooter) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheetDialog.setOnDismissListener {
            selectedMarker?.setIcon(requireContext().getDrawableToBitmapDescriptor(R.drawable.ic_scooter))
        }

        val dialogBinding = ViewUnlockDialogBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        dialogBinding.ringBtn.setOnClickListener {
            scooterViewModel.ringScooter(scooter.id)
        }

        dialogBinding.nfcBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    currentLocationData.location?.latitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    UnlockMethod.NFC
                )
            )
        }

        dialogBinding.qrBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    currentLocationData.location?.latitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    UnlockMethod.QR
                )
            )
        }

        dialogBinding.codeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            findNavController().navigate(
                MapFragmentDirections.actionMapFragmentToUnlockFragment(
                    currentLocationData.location?.longitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    currentLocationData.location?.latitude?.toFloat() ?: COORDINATE_SUBSTITUTE,
                    UnlockMethod.PIN
                )
            )
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showStartRideDialog() {
        val scooter: Scooter = scooterViewModel.scannedScooter.value ?: return

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheetDialog.setOnDismissListener {
            scooterViewModel.cancelScanScooter(scooter.number)
            startScootersUpdates()
        }

        val dialogBinding = ViewStartRideDialogBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        dialogBinding.startRideBtn.setOnClickListener {
            tripViewModel.startRide(
                scooter.number,
                currentLocationData.location?.latitude ?: .0,
                currentLocationData.location?.longitude ?: .0
            )

            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000L)
                userViewModel.getCurrentUser()
            }
            bottomSheetDialog.setOnDismissListener {}
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showCurrentRideDialogCollapsed() {
        val scooter = scooterViewModel.scannedScooter.value ?: return

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        val dialogBinding = ViewTripDetailsCollapsedBinding.inflate(layoutInflater, null, false)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        dialogBinding.lockBtn.text =
            if (isLockable) resources.getString(R.string.trip_details_lock_btn_text) else resources.getString(R.string.trip_details_unlock_btn_text)

        tripViewModel.seconds.observe(viewLifecycleOwner) { seconds ->
            val min = seconds / 60
            val minString = if (min / 10 == 0) "0$min" else min.toString()
            val hour = seconds / 3600
            val hourString = if (hour / 10 == 0) "0$hour" else hour.toString()
            dialogBinding.durationTV.text = TIME_TEMPLATE_MINUTES.format(hourString, minString)
        }

        tripViewModel.trip.observe(viewLifecycleOwner) { trip ->
            if (trip == null) return@observe
            val kms = trip.distance.toFloat() / 1000F
            dialogBinding.distanceTV.text = DISTANCE_TEMPLATE_SECONDS.format(kms)
        }

        dialogBinding.endRideBtn.setOnClickListener {
            tripViewModel.endRide(
                scooter.id,
                currentLocationData.location?.latitude ?: .0,
                currentLocationData.location?.longitude ?: .0
            )
            bottomSheetDialog.dismiss()
            startScootersUpdates()
        }

        dialogBinding.lockBtn.setOnClickListener {
            if (isLockable) {
                tripViewModel.lockRide(
                    scooter.id,
                    currentLocationData.location?.latitude ?: .0,
                    currentLocationData.location?.longitude ?: .0
                )
                dialogBinding.lockBtn.text = resources.getString(R.string.trip_details_unlock_btn_text)
            } else {
                tripViewModel.unlockRide(
                    scooter.id,
                    currentLocationData.location?.latitude ?: .0,
                    currentLocationData.location?.longitude ?: .0
                )
                dialogBinding.lockBtn.text = resources.getString(R.string.trip_details_lock_btn_text)
            }
            isLockable = !isLockable
        }

        dialogBinding.root.setOnClickListener {
            bottomSheetDialog.dismiss()
            showCurrentRideDialogExpanded()
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun showCurrentRideDialogExpanded() {
        val scooter = scooterViewModel.scannedScooter.value ?: return

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        bottomSheetDialog.setCancelable(false)

        val dialogBinding = ViewTripDetailsExpandedBinding.inflate(layoutInflater, null, false)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)
        dialogBinding.lockBtn.text =
            if (isLockable) resources.getString(R.string.trip_details_lock_btn_text) else resources.getString(R.string.trip_details_unlock_btn_text)

        tripViewModel.seconds.observe(viewLifecycleOwner) { seconds ->
            val sec = seconds % 60
            val secString = if (sec / 10 == 0) "0$sec" else sec.toString()
            val min = seconds / 60
            val minString = if (min / 10 == 0) "0$min" else min.toString()
            val hour = seconds / 3600
            val hourString = if (hour / 10 == 0) "0$hour" else hour.toString()
            dialogBinding.timeTV.text = TIME_TEMPLATE_SECONDS.format(hourString, minString, secString)
        }

        tripViewModel.trip.observe(viewLifecycleOwner) { trip ->
            if (trip == null) return@observe
            val kms = trip.distance.toFloat() / 1000F
            dialogBinding.distanceTV.text = DISTANCE_TEMPLATE_SECONDS.format(kms)
        }

        dialogBinding.lockBtn.setOnClickListener {
            if (isLockable) {
                tripViewModel.lockRide(
                    scooter.id,
                    currentLocationData.location?.latitude ?: .0,
                    currentLocationData.location?.longitude ?: .0
                )
                dialogBinding.lockBtn.text = resources.getString(R.string.trip_details_unlock_btn_text)
            } else {
                tripViewModel.unlockRide(
                    scooter.id,
                    currentLocationData.location?.latitude ?: .0,
                    currentLocationData.location?.longitude ?: .0
                )
                dialogBinding.lockBtn.text = resources.getString(R.string.trip_details_lock_btn_text)
            }
            isLockable = !isLockable
        }

        dialogBinding.endRideBtn.setOnClickListener {
            tripViewModel.endRide(
                scooter.id,
                currentLocationData.location?.latitude ?: .0,
                currentLocationData.location?.longitude ?: .0
            )
            bottomSheetDialog.dismiss()
            startScootersUpdates()
        }

        dialogBinding.minimizeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
            showCurrentRideDialogCollapsed()
        }

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    private fun createAlerter(message: String) {
        Alerter.create(requireActivity())
            .setText(message)
            .setTextAppearance(R.style.AlertTextAppearance)
            .setBackgroundColorRes(R.color.primary_color)
            .enableSwipeToDismiss()
            .show()
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
        private const val CIRCLE_STROKE_WIDTH = 0F

        private val DEFAULT_LOCATION = LatLng(46.769441, 23.589922)
        private const val COORDINATE_SUBSTITUTE = 0F

        private const val SCOOTER_UPDATES_INTERVAL = 60000L
        private const val LOCATION_UPDATES_INTERVAL = 10000L
        private const val MARKER_ANCHOR = 0.5F

        private const val TIME_TEMPLATE_MINUTES = "%s:%s min"
        private const val TIME_TEMPLATE_SECONDS = "%s:%s:%s"
        private const val DISTANCE_TEMPLATE_SECONDS = "%.1f km"
    }
}
