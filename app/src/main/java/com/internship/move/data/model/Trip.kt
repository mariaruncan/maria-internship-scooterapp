package com.internship.move.data.model

import com.google.android.gms.maps.model.LatLng

data class Trip(
    val userId: String,
    val scooterId: String,
    val locations: List<LatLng>,
    val status: TripStatus,
    val duration: Int,
    val distance: Int,
    val cost: Int
)