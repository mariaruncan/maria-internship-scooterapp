package com.internship.move.network

import com.internship.move.network.model.LoginRequest
import com.internship.move.network.model.LoginResponse
import com.internship.move.network.model.RegisterRequest
import com.internship.move.network.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceApi {

    @POST("users/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}