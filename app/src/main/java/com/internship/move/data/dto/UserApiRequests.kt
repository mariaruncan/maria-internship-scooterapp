package com.internship.move.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequestDTO(
    @Json(name = "name") val name: String,
    @Json(name = "mail") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class LoginRequestDTO(
    @Json(name = "mail") val email: String,
    @Json(name = "password") val password: String
)
