package com.internship.move.ui.authentication

import androidx.lifecycle.ViewModel
import com.internship.move.repository.Repository

class AuthenticationViewModel(
    private val repo: Repository
) : ViewModel() {

    fun register(email: String, username: String, password: String) {
        //api call
        repo.setIsLoggedIn(true)
    }

    fun login(email: String, password: String) {
        // api call
        repo.setIsLoggedIn(true)
    }

    fun resetPassword(newPassword: String) {
        // api call
    }
}