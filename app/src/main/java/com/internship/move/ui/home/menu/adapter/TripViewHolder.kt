package com.internship.move.ui.home.menu.adapter

import android.location.Geocoder
import androidx.recyclerview.widget.RecyclerView
import com.internship.move.MoveApp
import com.internship.move.R
import com.internship.move.data.model.Trip
import com.internship.move.databinding.ItemTripBinding
import com.internship.move.utils.extensions.fromSecondsToList
import com.internship.move.utils.extensions.toKms

class TripViewHolder(private val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {

    private val geocoder: Geocoder by lazy { Geocoder(MoveApp.getAppContext()) }

    fun bind(trip: Trip) {
        val startLocation = trip.locations.firstOrNull()
        val endLocation = trip.locations.lastOrNull()

        if (startLocation != null) {
            val address = geocoder.getFromLocation(startLocation.latitude, startLocation.longitude, 1)?.firstOrNull()
            if (address != null) {
                binding.startLocationTV.text = MoveApp.getAppContext().getString(R.string.address_template).format(
                    address.thoroughfare,
                    address.subThoroughfare
                )
            }
        }

        if (endLocation != null) {
            val address = geocoder.getFromLocation(endLocation.latitude, endLocation.longitude, 1)?.firstOrNull()
            if (address != null) {
                binding.endLocationTV.text = MoveApp.getAppContext().getString(R.string.address_template).format(
                    address.thoroughfare,
                    address.subThoroughfare
                )
            }
        }

        val durationList = trip.duration.fromSecondsToList()
        val hoursString = if(durationList[0] / 10 == 0) "0${durationList[0]}" else durationList[0].toString()
        val minString = if(durationList[1] / 10 == 0) "0${durationList[1]}" else durationList[1].toString()
        binding.timeTV.text = MoveApp.getAppContext().getString(R.string.time_minutes_template).format(
            hoursString,
            minString
        )

        binding.distanceTV.text = MoveApp.getAppContext().getString(R.string.distance_template).format(
            trip.distance.toKms()
        )
    }
}