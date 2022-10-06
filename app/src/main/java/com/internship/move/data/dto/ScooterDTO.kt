package com.internship.move.data.dto

import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.model.BookedStatus
import com.internship.move.data.model.LockedStatus
import com.internship.move.data.model.Scooter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScooterDTO(
    @Json(name = "location") val location: LocationDTO,
    @Json(name = "_id") val scooterId: String,
    @Json(name = "scootNumber") val scooterNumber: Int,
    @Json(name = "battery") val batteryLevel: Int,
    @Json(name = "bookedStatus") val bookedStatus: String,
    @Json(name = "lockedStatus") val lockedStatus: String,
) {

    fun toScooter(): Scooter =
        Scooter(
            this.scooterId,
            this.scooterNumber,
            LatLng(this.location.coordinates[1], this.location.coordinates[0]),
            this.batteryLevel,
            BookedStatus.fromString(this.bookedStatus),
            LockedStatus.fromString(this.lockedStatus)
        )
}