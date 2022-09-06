package com.internship.move.network

import com.internship.move.network.model.AddLicenseResponse
import com.internship.move.network.model.LoginRequest
import com.internship.move.network.model.LoginResponse
import com.internship.move.network.model.RegisterRequest
import com.internship.move.network.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ServiceApi {

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @Multipart
    @PUT("users/login")
    suspend fun addDrivingLicense(@Part("token") token: RequestBody, @Part productImage: MultipartBody.Part): AddLicenseResponse
}