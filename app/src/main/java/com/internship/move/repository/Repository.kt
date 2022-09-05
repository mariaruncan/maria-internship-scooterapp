package com.internship.move.repository

import com.internship.move.network.ServiceApi
import com.internship.move.network.model.ErrorResponse
import com.internship.move.network.model.LoginRequest
import com.internship.move.utils.InternalStorageManager
import java.lang.Exception

class Repository(
    private val internalStorageManager: InternalStorageManager,
    private val service: ServiceApi
) {

    fun getIsLoggedIn() = internalStorageManager.getIsLoggedIn()

    fun setIsLoggedIn(value: Boolean) {
        internalStorageManager.setIsLoggedIn(value)
    }

    fun getHasViewedOnboarding() = internalStorageManager.getHasViewedOnboarding()

    fun setHasViewedOnboarding(value: Boolean) {
        internalStorageManager.setHasSeenOnboarding(value)
    }

    suspend fun login(email: String, password: String) {
        try{
            val response = service.login(LoginRequest(email, password))
            internalStorageManager.setIsLoggedIn(true)
        }
        catch (e: Exception) {
            println(e.message)
        }
    }
}