package com.internship.move.feature.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HelpInfo(
    val message: String
) : Parcelable