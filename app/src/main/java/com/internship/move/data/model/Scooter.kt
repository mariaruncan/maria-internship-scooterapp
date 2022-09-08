package com.internship.move.data.model

import com.google.android.gms.maps.model.LatLng

data class Scooter(
    val id: String,
    val number: Int,
    val latLng: LatLng,
    val batteryLevel: Int,
    val bookedStatus: String,
    val lockedStatus: String,
)
