package com.internship.move.network

import com.internship.move.data.dto.CoordinatesDTO
import com.internship.move.data.dto.EndTripResponseDTO
import com.internship.move.data.dto.MultipleTripsResponseDTO
import com.internship.move.data.dto.TripResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TripApi {

    @POST("trips/startPhysical/{id}")
    suspend fun startRide(@Path("id") scooterId: String, @Body coordinates: CoordinatesDTO)

    @PATCH("trips/endPhysical/{id}")
    suspend fun endRide(@Path("id") scooterId: String, @Body coordinates: CoordinatesDTO): EndTripResponseDTO

    @PATCH("trips/lockPhysical/{id}")
    suspend fun lockRide(@Path("id") scooterId: String, @Body coordinates: CoordinatesDTO)

    @PATCH("trips/unlockPhysical/{id}")
    suspend fun unlockRide(@Path("id") scooterId: String, @Body coordinates: CoordinatesDTO)

    @GET("trips/current/{id}")
    suspend fun getCurrentTrip(@Path("id") scooterId: String): TripResponseDTO

    @GET("trips/me")
    suspend fun getUserTrips(): MultipleTripsResponseDTO
}