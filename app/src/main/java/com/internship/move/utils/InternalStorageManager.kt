package com.internship.move.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class InternalStorageManager(context: Context) {

    private val sharedPref = context.getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE)

    fun getToken() = sharedPref.getString(KEY_SESSION_TOKEN, null)

    fun setToken(value: String?) {
        sharedPref.edit()
            .putString(KEY_SESSION_TOKEN, value)
            .apply()
    }

    fun getHasViewedOnboarding() = sharedPref.getBoolean(KEY_HAS_VISITED_ONBOARDING, false)

    fun setHasSeenOnboarding(value: Boolean) {
        sharedPref.edit()
            .putBoolean(KEY_HAS_VISITED_ONBOARDING, value)
            .apply()
    }

    fun getHasDrivingLicense(): Boolean {
        val hasDrivingLicense = sharedPref.getBoolean(KEY_HAS_DRIVING_LICENSE, false)
        if (!hasDrivingLicense) setToken(null)
        return hasDrivingLicense
    }

    fun setHasDrivingLicense(value: Boolean) {
        sharedPref.edit()
            .putBoolean(KEY_HAS_DRIVING_LICENSE, value)
            .apply()
    }

    fun setScooterId(value: String? = null) {
        sharedPref.edit()
            .putString(KEY_SCOOTER_ID, value)
            .apply()
    }

    fun getScooterId(): String? = sharedPref.getString(KEY_SCOOTER_ID, null)

    fun setScooterNumber(value: Int = 0) {
        sharedPref.edit()
            .putInt(KEY_SCOOTER_NUMBER, value)
            .apply()
    }

    fun getScooterNumber(): Int = sharedPref.getInt(KEY_SCOOTER_NUMBER, 0)

    fun clearApp() {
        setToken(null)
        setHasDrivingLicense(false)
        setScooterNumber(0)
        setScooterId(null)
    }

    companion object {
        private const val KEY_APP_PREFERENCES = "com.internship.move.KEY_APP_PREFERENCES"
        private const val KEY_SESSION_TOKEN = "KEY_SESSION_TOKEN"
        private const val KEY_HAS_VISITED_ONBOARDING = "KEY_HAS_VISITED_ONBOARDING"
        private const val KEY_HAS_DRIVING_LICENSE = "KEY_HAS_DRIVING_LICENSE"
        private const val KEY_SCOOTER_ID = "KEY_SCOOTER_ID"
        private const val KEY_SCOOTER_NUMBER = "KEY_SCOOTER_NUMBER"
    }
}