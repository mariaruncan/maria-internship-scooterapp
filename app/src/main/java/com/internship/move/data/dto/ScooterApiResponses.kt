package com.internship.move.data.dto

import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.model.Scooter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScootersResponseDTO(
    @Json(name = "scooters") val scooters: List<ScooterDTO>
)

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
            LatLng(this.location.coordinates[0], this.location.coordinates[1]),
            this.batteryLevel,
            this.bookedStatus,
            this.lockedStatus
        )
}

@JsonClass(generateAdapter = true)
data class ScanResponseDTO(
    @Json(name = "updatedScooter") val scooter: ScooterDTO,
    @Json(name = "updatedUser") val user: UserDTO
)

@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "coordinates") val coordinates: List<Double>
)
