package com.internship.move.repository

import com.internship.move.utils.InternalStorageManager

class Repository(
    private val internalStorageManager: InternalStorageManager
) {

    fun getIsLoggedIn() = internalStorageManager.getIsLoggedIn()

    fun setIsLoggedIn(value: Boolean) {
        internalStorageManager.setIsLoggedIn(value)
    }

    fun getHasViewedOnboarding() = internalStorageManager.getHasViewedOnboarding()

    fun setHasViewedOnboarding(value: Boolean) {
        internalStorageManager.setHasSeenOnboarding(value)
    }
}