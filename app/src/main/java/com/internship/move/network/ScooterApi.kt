package com.internship.move.network

import com.internship.move.data.dto.GetScootersResponseDTO
import retrofit2.http.GET

interface ScooterApi {

    @GET("scooters/getScooters")
    suspend fun getAllScooters(): GetScootersResponseDTO
}
