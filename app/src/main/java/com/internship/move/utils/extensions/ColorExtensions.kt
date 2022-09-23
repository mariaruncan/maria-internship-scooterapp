package com.internship.move.utils.extensions

fun Int.setTransparency(alpha: Int) = (alpha shl 24) or (this and 0xFFFFFF)