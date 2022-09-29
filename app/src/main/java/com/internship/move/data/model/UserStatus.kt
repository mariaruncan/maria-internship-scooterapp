package com.internship.move.data.model

enum class UserStatus {
    FREE,
    SCANNED,
    BUSY;

    companion object{
        fun toString(status: UserStatus) = status.name.lowercase()

        fun fromString(status: String) = when (status) {
            "free" -> FREE
            "scanned" -> SCANNED
            "busy" -> BUSY
            else -> throw IllegalArgumentException("Wrong status!")
        }
    }
}