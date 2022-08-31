package com.internship.move.ui.home

import androidx.lifecycle.ViewModel
import com.internship.move.repository.Repository

class MainViewModel(
    private val repo: Repository
) : ViewModel() {

    fun logout() {
        repo.setIsLoggedIn(false)
    }

    fun clearApp() {
        repo.setIsLoggedIn(false)
        repo.setHasViewedOnboarding(false)
    }
}