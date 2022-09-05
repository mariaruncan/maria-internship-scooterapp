package com.internship.move.network

import com.internship.move.network.model.*
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceApi {

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): ErrorResponse
}