package com.internship.move.ui.onboarding.adapter

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardingPageDetails(
    @StringRes
    val titleId: Int,
    @StringRes
    val descriptionId: Int,
    @DrawableRes
    val backgroundId: Int,
    val isLastPage: Boolean = false
) : Parcelable