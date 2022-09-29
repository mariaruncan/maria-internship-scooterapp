package com.internship.move.network

import com.internship.move.data.dto.EndRideRequestDTO
import com.internship.move.data.dto.StartRideRequestDTO
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TripApi {

    @POST("trip")
    suspend fun startRide(@Body startRideRequest: StartRideRequestDTO)

    @PATCH("trips/end/{id}")
    suspend fun endRide(@Path("id") scooterId: String, @Body endRideRequest: EndRideRequestDTO)
}