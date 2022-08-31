package com.internship.move.ui.splash

import androidx.lifecycle.ViewModel
import com.internship.move.repository.Repository

class SplashViewModel(
    private val repo: Repository
) : ViewModel() {

    fun getIsLoggedIn() = repo.getIsLoggedIn()

    fun getHasViewedOnboarding() = repo.getHasViewdOnboarding()
}