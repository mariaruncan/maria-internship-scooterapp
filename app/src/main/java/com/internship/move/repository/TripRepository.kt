package com.internship.move.repository

import com.internship.move.data.dto.CoordinatesDTO
import com.internship.move.data.dto.StartRideRequestDTO
import com.internship.move.data.model.Trip
import com.internship.move.network.TripApi

class TripRepository(
    private val api: TripApi
) {

    suspend fun startRide(scooterNumber: Int, latitude: Double, longitude: Double) {
        api.startRide(StartRideRequestDTO(scooterNumber, latitude, longitude))
    }

    suspend fun endRide(scooterId: String, latitude: Double, longitude: Double) {
        api.endRide(scooterId, CoordinatesDTO(latitude, longitude))
    }

    suspend fun lockRide(scooterId: String, latitude: Double, longitude: Double) {
        api.lockRide(scooterId, CoordinatesDTO(latitude, longitude))
    }

    suspend fun unlockRide(scooterId: String, latitude: Double, longitude: Double) {
        api.unlockRide(scooterId, CoordinatesDTO(latitude, longitude))
    }

    suspend fun getCurrentTrip(scooterId: String): Trip = api.getCurrentTrip(scooterId).toTrip()

    suspend fun getUserTrips(): List<Trip> = api.getUserTrips().toListOfTrips()
}