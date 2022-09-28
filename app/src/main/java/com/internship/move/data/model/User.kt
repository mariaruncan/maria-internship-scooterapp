package com.internship.move.data.model

data class User(
    val name: String,
    val email: String,
    val status: UserStatus,
    val hasDrivingLicense: Boolean,
    val numberOfTrips: Int = 0,
    var scooter: Scooter? = null
)

enum class UserStatus {
    FREE,
    SCANNED;

    companion object{
        private const val FREE_STRING = "free"
        private const val SCANNED_STRING = "scanned"

        fun toString(status: UserStatus) = status.name.lowercase()

        fun fromString(status: String) = when (status) {
            FREE_STRING -> FREE
            SCANNED_STRING -> SCANNED
            else -> throw IllegalArgumentException("Wrong status!")
        }
    }
}
