package com.internship.move.data.model

enum class BookedStatus {
    FREE,
    BOOKED;

    companion object {
        private const val FREE_STRING = "free"
        private const val BOOKED_STRING = "booked"

        fun toString(status: BookedStatus): String = status.name.lowercase()

        fun fromString(statusString: String): BookedStatus =
            when (statusString) {
                FREE_STRING -> FREE
                BOOKED_STRING -> BOOKED
                else -> throw IllegalArgumentException("Wrong status!")
            }
    }
}

enum class LockedStatus {
    AVAILABLE,
    DISABLED,
    LOCKED;

    companion object {
        private const val AVAILABLE_STRING = "available"
        private const val DISABLED_STRING = "disabled"
        private const val LOCKED_STRING = "locked"

        fun toString(status: LockedStatus): String = status.name.lowercase()

        fun fromString(statusString: String): LockedStatus =
            when (statusString) {
                AVAILABLE_STRING -> AVAILABLE
                DISABLED_STRING -> DISABLED
                LOCKED_STRING -> LOCKED
                else -> throw IllegalArgumentException("Wrong status!")
            }
    }
}