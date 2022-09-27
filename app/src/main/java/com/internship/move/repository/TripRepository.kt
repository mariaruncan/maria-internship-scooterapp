package com.internship.move.repository

import com.internship.move.data.dto.EndRideRequestDTO
import com.internship.move.data.dto.StartRideRequestDTO
import com.internship.move.network.TripApi

class TripRepository(
    private val api: TripApi
) {

    suspend fun startRide(scooterNumber: Int, latitude: Double, longitude: Double) {
        api.startRide(StartRideRequestDTO(scooterNumber, latitude, longitude))
    }

    suspend fun endRide(scooterId: String, latitude: Double, longitude: Double) {
        api.endRide(scooterId, EndRideRequestDTO(latitude, longitude))
    }
}