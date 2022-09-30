package com.internship.move.data.dto

import com.internship.move.data.model.User
import com.internship.move.data.model.UserStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    @Json(name = "_id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "mail") val email: String,
    @Json(name = "status") val status: String,
    @Json(name = "password") val encryptedPassword: String,
    @Json(name = "productImage") val drivingLicense: String?
) {
    fun toUser(): User = User(name, email, UserStatus.fromString(status), drivingLicense != null)
}