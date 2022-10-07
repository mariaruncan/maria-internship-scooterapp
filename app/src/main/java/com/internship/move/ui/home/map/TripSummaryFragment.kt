package com.internship.move.ui.home.map

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.internship.move.R
import com.internship.move.data.model.Trip
import com.internship.move.data.model.TripStatus
import com.internship.move.databinding.FragmentTripSummaryBinding
import com.internship.move.ui.home.TripViewModel
import com.internship.move.utils.extensions.fromSecondsToList
import com.internship.move.utils.extensions.toKms
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class TripSummaryFragment : Fragment(R.layout.fragment_trip_summary) {

    private val binding by viewBinding(FragmentTripSummaryBinding::bind)
    private val tripViewModel by koinNavGraphViewModel<TripViewModel>(R.id.home_graph)
    private val geocoder by lazy { Geocoder(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            tripViewModel.trip.value = null
            findNavController().navigateUp()
        }

        binding.mapBtn.setOnClickListener {
            tripViewModel.trip.value = null
            findNavController().navigateUp()
        }

        tripViewModel.trip.observe(viewLifecycleOwner) { trip ->
            displayTripDetails(trip)
        }
    }

    private fun displayTripDetails(trip: Trip?) {
        if (trip == null || trip.status != TripStatus.ENDED) return
        println(trip.toString())
        val startLocation = trip.locations.firstOrNull()
        val endLocation = trip.locations.lastOrNull()

        if (startLocation != null) {
            val address = geocoder.getFromLocation(startLocation.latitude, startLocation.longitude, 1)?.firstOrNull()
            binding.startLocationTV.text = getString(R.string.address_template).format(address?.thoroughfare, address?.subThoroughfare)
        }

        if (endLocation != null) {
            val address = geocoder.getFromLocation(endLocation.latitude, endLocation.longitude, 1)?.firstOrNull()
            binding.endLocationTV.text = getString(R.string.address_template).format(address?.thoroughfare, address?.subThoroughfare)
        }

        val duration = trip.duration.fromSecondsToList()
        println(duration)
        val hourString = if (duration[0] / 10 == 0) "0${duration[0]}" else duration[0].toString()
        val minString = if (duration[1] / 10 == 0) "0${duration[1]}" else duration[1].toString()
        binding.timeTV.text = getString(R.string.time_minutes_template).format(hourString, minString)

        val kms = trip.distance.toKms()
        println(kms)
        binding.distanceTV.text = getString(R.string.distance_template).format(kms)
    }
}