package com.internship.move.data.dto

import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.model.Trip
import com.internship.move.data.model.TripStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TripDTO(
    @Json(name = "_id") val tripId: String,
    @Json(name = "startLocation") val startLocation: LocationDTO,
    @Json(name = "endLocation") val endLocation: LocationDTO,
    @Json(name = "userId") val userId: String,
    @Json(name = "scooterId") val scooterId: String,
    @Json(name = "status") val status: String,
    @Json(name = "cost") val cost: Float,
    @Json(name = "distance") val distance: Int,
    @Json(name = "duration") val duration: Int,
    @Json(name = "allLocations") val allLocations: List<LocationDTO>
) {

    fun toTrip(): Trip {
        val locations = mutableListOf<LatLng>()
        allLocations.forEach { location ->
            locations.add(LatLng(location.coordinates[1], location.coordinates[0]))
        }
        return Trip(
            tripId,
            userId,
            scooterId,
            locations,
            TripStatus.fromString(status),
            duration,
            distance,
            cost
        )
    }
}
