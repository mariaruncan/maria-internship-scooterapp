package com.internship.move.ui.splash

import androidx.lifecycle.ViewModel
import com.internship.move.repository.Repository

class SplashViewModel(
    private val repo: Repository
) : ViewModel() {

    val isLoggedIn: Boolean
        get() = repo.getIsLoggedIn()

    val hasViewedOnboarding: Boolean
        get() = repo.getHasViewedOnboarding()
}