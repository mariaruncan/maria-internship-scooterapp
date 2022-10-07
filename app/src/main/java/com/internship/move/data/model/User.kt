package com.internship.move.data.model

data class User(
    val name: String,
    val email: String,
    val status: UserStatus,
    val hasDrivingLicense: Boolean,
    val numberOfTrips: Int? = null
)
