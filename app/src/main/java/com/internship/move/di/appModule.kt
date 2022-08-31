package com.internship.move.di

import com.internship.move.repository.Repository
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.ui.home.MainViewModel
import com.internship.move.ui.onboarding.OnboardingViewModel
import com.internship.move.ui.splash.SplashViewModel
import com.internship.move.utils.InternalStorageManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModels = module {
    viewModel { SplashViewModel(repo = get()) }
    viewModel { OnboardingViewModel(repo = get()) }
    viewModel { AuthenticationViewModel(repo = get()) }
    viewModel { MainViewModel(repo = get()) }
}

val repositories = module {
    single { Repository(internalStorageManager = get()) }
}

val storage = module {
    single { InternalStorageManager(androidContext()) }
}