package com.internship.move.ui.main.viewmodel

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import com.internship.move.MainActivity
import com.internship.move.util.Constants.SharedPref.KEY_APP_PREFERENCES
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_AUTHENTICATION
import com.internship.move.util.Constants.SharedPref.KEY_HAS_VISITED_ONBOARDING

class MainViewModel : ViewModel() {

    private val sharedPref by lazy { MainActivity.appContext.getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE) }
    private var hasVisitedOnboarding: Boolean
        get() {
            return sharedPref.getBoolean(KEY_HAS_VISITED_ONBOARDING, false)
        }
        set(value) {
            sharedPref.edit()
                .putBoolean(KEY_HAS_VISITED_ONBOARDING, value)
                .apply()
        }
    private var hasVisitedAuthentication: Boolean
        get() {
            return sharedPref.getBoolean(KEY_HAS_VISITED_AUTHENTICATION, false)
        }
        set(value) {
            sharedPref.edit()
                .putBoolean(KEY_HAS_VISITED_AUTHENTICATION, value)
                .apply()
        }

    fun getIsLoggedIn() = hasVisitedAuthentication

    fun getHasViewedOnboarding() = hasVisitedOnboarding

    fun notifyHasViewedOnboarding() {
        hasVisitedOnboarding = true
    }

    fun login(email: String, password: String) {
        // api call
        hasVisitedAuthentication = true
    }

    fun register(email: String, username: String, password: String) {
        // api call
        hasVisitedAuthentication = true
    }

    fun logout() {
        hasVisitedAuthentication = false
    }

    fun clearApp() {
        hasVisitedAuthentication = false
        hasVisitedOnboarding = false
    }
}