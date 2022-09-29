package com.internship.move.data.model

import android.location.Location
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker

data class CurrentLocationData(
    var marker: Marker? = null,
    var circle: Circle? = null,
    var location: Location? = null
)