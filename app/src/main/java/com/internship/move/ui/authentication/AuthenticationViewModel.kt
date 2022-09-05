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

    suspend fun login(email: String, password: String) {
        repo.login(email, password)
    }

    fun resetPassword(newPassword: String) {
        // api call
    }
}