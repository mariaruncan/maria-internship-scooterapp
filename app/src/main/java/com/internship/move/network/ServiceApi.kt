package com.internship.move.network

import com.internship.move.network.dto.AddLicenseResponseDTO
import com.internship.move.network.dto.LoginRequestDTO
import com.internship.move.network.dto.LoginResponseDTO
import com.internship.move.network.dto.RegisterRequestDTO
import com.internship.move.network.dto.RegisterResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ServiceApi {

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequestDTO): RegisterResponseDTO

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): LoginResponseDTO

    @Multipart
    @PUT("users/login")
    suspend fun addDrivingLicense(@Part("token") token: RequestBody, @Part productImage: MultipartBody.Part): AddLicenseResponseDTO
}