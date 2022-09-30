package com.internship.move.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDTO(
    @Json(name = "coordinates") val coordinates: List<Double>
)