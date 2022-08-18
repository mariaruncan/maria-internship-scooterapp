package com.internship.move.feature.onboarding

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class PageDetails(
    @StringRes
    val titleId: Int,
    @StringRes
    val descriptionId: Int,
    @DrawableRes
    val backgroundId: Int,
    val isLastPage: Boolean = false
) : Parcelable