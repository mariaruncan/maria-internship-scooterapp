package com.internship.move.utils.extensions

fun Int.fromSecondsToList(): List<Int> {
    val hours = this / 3600
    val minutes = this / 60
    val seconds = this % 60
    return listOf(hours, minutes, seconds)
}

fun Int.toKms(): Float = this / 1000F
