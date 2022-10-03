package com.internship.move.data.model

enum class BookedStatus {
    AVAILABLE,
    SCANNED,
    BOOKED,
    LOCKED,
    DISABLED;

    companion object {
        private const val AVAILABLE_STRING = "available"
        private const val SCANNED_STRING = "scanned"
        private const val BOOKED_STRING = "booked"
        private const val LOCKED_STRING = "locked"
        private const val DISABLED_STRING = "disabled"

        fun toString(status: BookedStatus): String = status.name.lowercase()

        fun fromString(statusString: String): BookedStatus =
            when (statusString) {
                AVAILABLE_STRING -> AVAILABLE
                SCANNED_STRING -> SCANNED
                BOOKED_STRING -> BOOKED
                LOCKED_STRING -> LOCKED
                DISABLED_STRING -> DISABLED
                else -> throw IllegalArgumentException("Wrong status!")
            }
    }
}

enum class LockedStatus {
    UNLOCKED,
    LOCKED;

    companion object {
        private const val UNLOCKED_STRING = "unlocked"
        private const val LOCKED_STRING = "locked"

        fun toString(status: LockedStatus): String = status.name.lowercase()

        fun fromString(statusString: String): LockedStatus =
            when (statusString) {
                UNLOCKED_STRING -> UNLOCKED
                LOCKED_STRING -> LOCKED
                else -> throw IllegalArgumentException("Wrong status!")
            }
    }
}