package com.internship.move.ui.home.menu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.internship.move.data.model.Trip

object TripsDiffUtilItemCallback : DiffUtil.ItemCallback<Trip>() {

    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem.tripId == newItem.tripId

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem == newItem
}