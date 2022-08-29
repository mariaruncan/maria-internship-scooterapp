package com.internship.move

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _appContext = this.application
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        findNavController(R.id.nav_host_fragment).handleDeepLink(intent)
    }

    companion object {
        private var _appContext: Application? = null
        val appContext: Application
            get() = _appContext ?: throw IllegalArgumentException("App context not initialized")
    }
}