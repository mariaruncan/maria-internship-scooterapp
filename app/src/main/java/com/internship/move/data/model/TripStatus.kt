package com.internship.move.data.model

enum class TripStatus {
    IN_PROGRESS,
    ENDED,
    PAUSED;

    companion object {
        private const val IN_PROGRESS_STRING = "in progress"
        private const val ENDED_STRING = "ended"
        private const val PAUSED_STRING = "paused"

        fun toString(status: TripStatus): String = status.name.lowercase().replace("_", " ", false)

        fun fromString(statusString: String): TripStatus =
            when (statusString) {
                IN_PROGRESS_STRING -> IN_PROGRESS
                ENDED_STRING -> ENDED
                PAUSED_STRING -> PAUSED
                else -> throw IllegalArgumentException("Wrong status!")
            }
    }
}