package com.internship.move.ui.onboarding

import androidx.lifecycle.ViewModel
import com.internship.move.repository.Repository

class OnboardingViewModel(
    private val repo: Repository
): ViewModel() {

    fun notifyHasViewedOnboarding() {
        repo.setHasViewedOnboarding(true)
    }
}