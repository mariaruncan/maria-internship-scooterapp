package com.internship.move.network

import com.internship.move.data.dto.AddLicenseResponseDTO
import com.internship.move.data.dto.LoginRequestDTO
import com.internship.move.data.dto.LoginResponseDTO
import com.internship.move.data.dto.RegisterRequestDTO
import com.internship.move.data.dto.RegisterResponseDTO
import com.internship.move.data.dto.UserDTO
import com.internship.move.data.dto.UserResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequestDTO): RegisterResponseDTO

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): LoginResponseDTO

    @Multipart
    @PUT("users/addImage")
    suspend fun addDrivingLicense(@Part productImage: MultipartBody.Part): AddLicenseResponseDTO

    @GET("users/me")
    suspend fun getCurrentUser(): UserResponseDTO
}
