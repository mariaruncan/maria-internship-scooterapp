package com.internship.move.ui.splash

import androidx.lifecycle.ViewModel
import com.internship.move.utils.InternalStorageManager

class SplashViewModel(
    private val internalStorageManager: InternalStorageManager
) : ViewModel() {

    val sessionToken: String?
        get() = internalStorageManager.getToken()

    val hasViewedOnboarding: Boolean
        get() = internalStorageManager.getHasViewedOnboarding()

    val hasDrivingLicense: Boolean
        get() = internalStorageManager.getHasDrivingLicense()
}