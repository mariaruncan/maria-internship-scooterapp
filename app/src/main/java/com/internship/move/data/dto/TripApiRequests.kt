package com.internship.move.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartRideRequestDTO(
    @Json(name = "scootNumber") val scooterNumber: Int,
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
)

@JsonClass(generateAdapter = true)
data class EndRideRequestDTO(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
)