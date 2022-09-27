package com.internship.move.data.model

data class User(
    val name: String,
    val email: String,
    val status: UserStatus,
    val hasDrivingLicense: Boolean,
    var numberOfTrips: Int = 0,
    var scooter: Scooter? = null
)

enum class UserStatus {
    FREE,
    SCANNED;

    companion object{
        fun toString(status: UserStatus) = status.name.lowercase()

        fun fromString(status: String) = when (status) {
            "free" -> FREE
            "scanned" -> SCANNED
            else -> throw IllegalArgumentException("Wrong status!")
        }
    }
}
