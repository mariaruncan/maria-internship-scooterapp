package com.internship.move.network

import com.internship.move.data.dto.AddLicenseResponseDTO
import com.internship.move.data.dto.LoginRequestDTO
import com.internship.move.data.dto.LoginResponseDTO
import com.internship.move.data.dto.RegisterRequestDTO
import com.internship.move.data.dto.RegisterResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {

    @POST("gateway/register")
    suspend fun register(@Body registerRequest: RegisterRequestDTO): RegisterResponseDTO

    @POST("gateway/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): LoginResponseDTO

    @Multipart
    @PUT("users/addImage")
    suspend fun addDrivingLicense(@Part productImage: MultipartBody.Part): AddLicenseResponseDTO
}
