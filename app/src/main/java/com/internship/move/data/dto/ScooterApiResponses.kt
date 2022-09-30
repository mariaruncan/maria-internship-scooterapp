package com.internship.move.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScootersResponseDTO(
    @Json(name = "scooters") val scooters: List<ScooterDTO>
)

@JsonClass(generateAdapter = true)
data class ScanResponseDTO(
    @Json(name = "updatedScooter") val scooter: ScooterDTO,
    @Json(name = "updatedUser") val user: UserDTO
)
