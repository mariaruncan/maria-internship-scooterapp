package com.internship.move.data.model

data class User(
    val name: String,
    val email: String,
    val status: String,
    val hasDrivingLicense: Boolean,
    var numberOfTrips: Int = 0,
    var scooter: Scooter? = null
)