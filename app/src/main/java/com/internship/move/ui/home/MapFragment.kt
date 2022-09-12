package com.internship.move.ui.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.internship.move.R
import com.internship.move.data.model.Scooter
import com.internship.move.databinding.FragmentMapBinding
import com.internship.move.utils.BitmapHelper
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationGranted: Boolean = true
    private lateinit var supportMapFragment: SupportMapFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLocationPermissions()
        if (locationGranted) {
            initObservers()
            supportMapFragment = childFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
            viewModel.getAllScooters()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            displayCurrentLocation()
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

    private fun initObservers() {
        viewModel.scootersList.observe(viewLifecycleOwner) { scootersList ->
            displayScooters(scootersList)
        }
    }

    private fun displayScooters(scooters: List<Scooter>) {
        supportMapFragment.getMapAsync { map ->
            map.setOnInfoWindowCloseListener { marker ->
                marker.setIcon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter))
            }
            map.clear()
            scooters.forEach { scooter ->
                map.addMarker(
                    MarkerOptions()
                        .position(scooter.latLng)
                        .icon(BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_scooter))
                )
                    ?.tag = scooter
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun displayCurrentLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
            val position = LatLng(it.latitude, it.longitude)
            supportMapFragment.getMapAsync { map ->
                map.addMarker(
                    MarkerOptions().position(position).title("current position")
                )
                    ?.tag = null
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10F))
            }
        }
    }
}
