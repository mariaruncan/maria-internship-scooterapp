package com.internship.move.data.dto

import com.internship.move.data.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponseDTO(
    @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class RegisterResponseDTO(
    @Json(name = "registeredUser") val user: UserDTO,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class LoginResponseDTO(
    @Json(name = "user") val user: UserDTO,
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class AddLicenseResponseDTO(
    @Json(name = "existingUser") val user: UserDTO
)

@JsonClass(generateAdapter = true)
data class UserResponseDTO(
    @Json(name = "existingUser") val user: UserDTO,
    @Json(name = "nrOfTrips") val numberOfTrips: Int
) {

    fun toUser(): User = user.toUser().copy(numberOfTrips = numberOfTrips)
}