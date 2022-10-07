package com.internship.move.ui.home.menu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.internship.move.data.model.Trip
import com.internship.move.databinding.ItemTripBinding

class TripsAdapter : ListAdapter<Trip, TripViewHolder>(TripsDiffUtilItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder =
        TripViewHolder(
            ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}