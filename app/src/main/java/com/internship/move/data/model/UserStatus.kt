package com.internship.move.data.model

enum class UserStatus {
    FREE,
    SCANNED,
    BUSY;

    companion object {
        private const val FREE_STRING = "free"
        private const val SCANNED_STRING = "scanned"
        private const val BUSY_STRING = "busy"

        fun toString(status: UserStatus) = status.name.lowercase()

        fun fromString(status: String): UserStatus = when (status) {
            FREE_STRING -> FREE
            SCANNED_STRING -> SCANNED
            BUSY_STRING -> BUSY
            else -> throw IllegalArgumentException("Wrong status!")
        }
    }
}