package com.internship.move.network

import com.internship.move.data.dto.ScanResponseDTO
import com.internship.move.data.dto.ScootersResponseDTO
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ScooterApi {

    @GET("scooters")
    suspend fun getAllScooters(): ScootersResponseDTO

    @PATCH("scooters/scan")
    suspend fun scanScooter(
        @Query("method") method: String,
        @Query("id") id: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): ScanResponseDTO

    @PATCH("scooters/cancel")
    suspend fun cancelScanScooter(@Query("id") id: Int)

    @PUT("scooters/{id}/beep")
    suspend fun beepScooter(@Path("id") scooterId: String)
}
