package com.internship.move.ui.home

import androidx.lifecycle.ViewModel
import com.internship.move.utils.InternalStorageManager

class MainViewModel(
    private val internalStorageManager: InternalStorageManager
) : ViewModel() {

    fun logout() {
        internalStorageManager.setToken(null)
        internalStorageManager.setHasDrivingLicense(false)
    }

    fun clearApp() {
        logout()
        internalStorageManager.setHasSeenOnboarding(false)
    }
}