package com.internship.move.ui.home.map

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.data.model.Scooter
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.databinding.ViewUnlockDialogBinding
import com.internship.move.ui.home.MainViewModel
import com.internship.move.utils.BitmapHelper
import com.internship.move.utils.extensions.setBatteryIcon
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(R.layout.fragment_map), GoogleMap.OnMapClickListener, OnClusterClickListener<Scooter>,
    ClusterManager.OnClusterItemClickListener<Scooter>, OnMapReadyCallback {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MainViewModel by viewModel()

    private var locationGranted: Boolean = true
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<Scooter>

    private var selectedMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLocationPermissions()
        if (locationGranted) {
            initMap(savedInstanceState)
        } else {
            Toast.makeText(requireContext(), "Location permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                locationGranted = result.map { it.value }.reduce { acc, other -> acc and other }
            }.launch(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        geocoder = Geocoder(requireContext())
    }

    private fun initObservers() {
        viewModel.scootersList.observe(viewLifecycleOwner) { scootersList ->
            displayScooters(scootersList)
        }
    }

    private fun initClustering() {
        clusterManager = ClusterManager<Scooter>(requireContext(), map)
        clusterManager.setOnClusterClickListener(this)
        clusterManager.setOnClusterItemClickListener(this)
        clusterManager.renderer = ScooterRenderer(requireContext(), map, clusterManager)

        map.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
    }

    @SuppressLint("MissingPermission")
    private fun displayCurrentLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
            val position = LatLng(it.latitude, it.longitude)
            binding.toolbar.title = geocoder.getFromLocation(it.latitude, it.longitude, 1)[0].locality
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .icon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_current_location))
            )
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, ZOOM_VALUE))
        }
    }

    private fun displayScooters(scooters: List<Scooter>) {
        clusterManager.clearItems()
        clusterManager.addItems(scooters)
        clusterManager.cluster()
    }

    private fun displayInfoWindow(scooter: Scooter) {
        val infoWindow = binding.scooterInfoWindow
        infoWindow.root.isVisible = true
        infoWindow.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        infoWindow.batteryIV.setBatteryIcon(scooter.batteryLevel)

        infoWindow.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        val address = geocoder.getFromLocation(scooter.latLng.latitude, scooter.latLng.longitude, 1)[0]
        infoWindow.addressTV.text = SCOOTER_ADDRESS_TEMPLATE.format(address.thoroughfare, address.subThoroughfare)

        // setButtonsListeners listeners
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
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setOnDismissListener {
            selectedMarker?.setIcon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter))
        }

        val dialogBinding = ViewUnlockDialogBinding.inflate(layoutInflater, null, false)
        dialogBinding.scooterNumberTV.text = SCOOTER_NUMBER_TEMPLATE.format(scooter.number)
        dialogBinding.batteryIV.setBatteryIcon(scooter.batteryLevel)
        dialogBinding.batteryTV.text = SCOOTER_BATTERY_TEMPLATE.format(scooter.batteryLevel)

        // set click listeners

        bottomSheetDialog.setContentView(dialogBinding.root)
        bottomSheetDialog.show()
    }

    override fun onClusterClick(cluster: Cluster<Scooter>?): Boolean {
        if (cluster != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.position, ZOOM_VALUE))
        }
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener { marker ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, ZOOM_VALUE))
            true
        }
        try {
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json)
            )
        } catch (e: Resources.NotFoundException) {
        }
        map.setOnMapClickListener(this)
        initClustering()
        initObservers()
        viewModel.getAllScooters()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        displayCurrentLocation()
    }

    override fun onClusterItemClick(item: Scooter?): Boolean {
        if (selectedMarker != null) {
            selectedMarker?.setIcon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter))
        }

        selectedMarker = (clusterManager.renderer as DefaultClusterRenderer<Scooter>).getMarker(item)
        if (selectedMarker != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedMarker!!.position, ZOOM_VALUE))
            selectedMarker?.setIcon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter_selected))

            if (item != null) {
                displayInfoWindow(item)
            }
        }
        return true
    }

    override fun onMapClick(p0: LatLng) {
        if (selectedMarker != null) {
            try {
                selectedMarker?.setIcon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter))
            } catch (ex: IllegalArgumentException) {
                // throws when infoWindow is visible, but the marker is in a cluster(no longer visible)
                Log.d("", ex.message, ex)
            }
            hideInfoWindow()
        }
        selectedMarker = null
    }

    companion object {
        private const val SCOOTER_NUMBER_TEMPLATE = "#%d"
        private const val SCOOTER_BATTERY_TEMPLATE = "%d%%"
        private const val SCOOTER_ADDRESS_TEMPLATE = "%s %s"

        private const val ZOOM_VALUE = 15F
    }
}
