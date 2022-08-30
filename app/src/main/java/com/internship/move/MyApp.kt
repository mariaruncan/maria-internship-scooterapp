package com.internship.move

import android.app.Application

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        _appContext = this
    }

    companion object {
        private var _appContext: Application? = null
        val appContext: Application
            get() = _appContext ?: throw IllegalArgumentException("App context not initialized")
    }
}