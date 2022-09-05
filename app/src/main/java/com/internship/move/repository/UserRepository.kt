package com.internship.move.repository

import com.internship.move.network.ServiceApi
import com.internship.move.network.model.LoginRequest
import com.internship.move.network.model.RegisterRequest

class UserRepository(
    private val api: ServiceApi
) {

    suspend fun register(name: String, email: String, password: String) = api.register(RegisterRequest(name, email, password))

    suspend fun login(email: String, password: String) = api.login(LoginRequest(email, password))
}