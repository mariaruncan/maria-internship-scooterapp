package com.internship.move.ui.home.menu.adapter

import android.location.Geocoder
import androidx.recyclerview.widget.RecyclerView
import com.internship.move.MoveApp
import com.internship.move.data.model.Trip
import com.internship.move.databinding.ItemTripBinding

class TripViewHolder(private val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {

    private val geocoder: Geocoder by lazy { Geocoder(MoveApp.getAppContext()) }

    fun bind(trip: Trip) {
        val startLocation = trip.locations.firstOrNull()
        val endLocation = trip.locations.lastOrNull()
        if(startLocation != null) {

        }
        binding.startLocationTV.text =
    }
}