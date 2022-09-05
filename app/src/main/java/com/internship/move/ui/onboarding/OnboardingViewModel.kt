package com.internship.move.ui.onboarding

import androidx.lifecycle.ViewModel
import com.internship.move.utils.InternalStorageManager

class OnboardingViewModel(
    private val internalStorageManager: InternalStorageManager
) : ViewModel() {

    fun setHasViewedOnboarding() {
        internalStorageManager.setHasSeenOnboarding(true)
    }
}