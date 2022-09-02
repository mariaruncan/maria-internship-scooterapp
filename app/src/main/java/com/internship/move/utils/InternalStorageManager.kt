package com.internship.move.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class InternalStorageManager(context: Context) {

    private val sharedPref = context.getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE)

    fun getIsLoggedIn() = sharedPref.getBoolean(KEY_HAS_VISITED_AUTHENTICATION, false)

    fun setIsLoggedIn(value: Boolean) {
        sharedPref.edit()
            .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, value)
            .apply()
    }

    fun getHasViewedOnboarding() = sharedPref.getBoolean(KEY_HAS_VISITED_ONBOARDING, false)

    fun setHasSeenOnboarding(value: Boolean) {
        sharedPref.edit()
            .putBoolean(KEY_HAS_VISITED_ONBOARDING, value)
            .apply()
    }

    companion object {
        private const val KEY_APP_PREFERENCES = "com.internship.move.KEY_APP_PREFERENCES"
        private const val KEY_HAS_VISITED_AUTHENTICATION = "KEY_HAS_VISITED_AUTHENTICATION"
        private const val KEY_HAS_VISITED_ONBOARDING = "KEY_HAS_VISITED_ONBOARDING"
    }
}