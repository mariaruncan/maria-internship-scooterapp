package com.internship.move.data.dto

import com.internship.move.data.model.User
import com.internship.move.data.model.UserStatus
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
data class UserDTO(
    @Json(name = "_id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "mail") val email: String,
    @Json(name = "status") val status: String,
    @Json(name = "password") val encryptedPassword: String,
    @Json(name = "productImage") val drivingLicense: String?
) {
    fun toUser() = User(name, email, UserStatus.fromString(status), drivingLicense != null)
}

@JsonClass(generateAdapter = true)
data class UserResponseDTO(
    @Json(name = "existingUser") val user: UserDTO,
    @Json(name = "nrOfTrips") val numberOfTrips: Int
) {

    fun toUser(): User{
        val user = user.toUser()
        user.numberOfTrips = numberOfTrips
        return user
    }
}