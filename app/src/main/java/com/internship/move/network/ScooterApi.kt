package com.internship.move.network

import com.internship.move.data.dto.ScootersResponseDTO
import retrofit2.http.GET

interface ScooterApi {

    @GET("scooters/getScooters")
    suspend fun getAllScooters(): ScootersResponseDTO
}