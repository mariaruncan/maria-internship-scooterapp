package com.internship.move

import android.app.Application
import com.internship.move.di.repositories
import com.internship.move.di.services
import com.internship.move.di.storage
import com.internship.move.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoveApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MoveApp)
            modules(
                viewModels,
                repositories,
                services,
                storage
            )
        }
    }
}