package com.internship.move.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponseDTO(
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class RegisterResponseDTO(
    @Json(name = "registered_user") val user: UserDTO,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class LoginResponseDTO(
    @Json(name = "user") val user: UserDTO,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class AddLicenseResponseDTO(
    @Json(name = "existing_user") val user: UserDTO
)

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "_id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "mail") val email: String,
    @Json(name = "status") val status: String,
    @Json(name = "password") val encryptedPassword: String,
    @Json(name = "productImage") val drivingLicense: String?
)