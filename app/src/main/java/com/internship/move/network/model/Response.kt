package com.internship.move.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @Json(name = "registered_user") val user: User,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "user") val user: User,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class AddLicenseResponse(
    @Json(name = "existing_user") val user: User
)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "_id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "mail") val email: String,
    @Json(name = "status") val status: String,
    @Json(name = "password") val encryptedPassword: String,
    @Json(name = "productImage") val drivingLicenseUri: String?
)