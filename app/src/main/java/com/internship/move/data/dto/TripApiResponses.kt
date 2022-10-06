package com.internship.move.data.dto

import com.internship.move.data.model.Trip
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TripResponseDTO(
    @Json(name = "trip") val trip: TripDTO
) {

    fun toTrip(): Trip = trip.toTrip()
}

@JsonClass(generateAdapter = true)
data class MultipleTripsResponseDTO(
    @Json(name = "trips") val trips: List<TripDTO>
) {
    fun toListOfTrips(): List<Trip> {
        val resultList = mutableListOf<Trip>()
        trips.forEach { trip ->
            resultList.add(trip.toTrip())
        }
        return resultList
    }
}

@JsonClass(generateAdapter = true)
data class EndTripResponseDTO(
    @Json(name = "endedTrip") val endedTrip: TripDTO
)