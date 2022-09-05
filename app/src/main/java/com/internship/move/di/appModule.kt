package com.internship.move.di

import com.internship.move.network.ServiceApi
import com.internship.move.repository.UserRepository
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.ui.home.MainViewModel
import com.internship.move.ui.onboarding.OnboardingViewModel
import com.internship.move.ui.splash.SplashViewModel
import com.internship.move.utils.InternalStorageManager
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val viewModels = module {
    viewModel { SplashViewModel(internalStorageManager = get()) }
    viewModel { OnboardingViewModel(internalStorageManager = get()) }
    viewModel { AuthenticationViewModel(repo = get(), internalStorageManager = get()) }
    viewModel { MainViewModel(internalStorageManager = get()) }
}

val repositories = module {
    single { UserRepository(api = get()) }
}

val services = module {
    single { getMoshi() }
    single { getOkHttpClient() }
    single { getRetrofit(moshi = get(), httpClient = get()) }
    single { getService(retrofit = get()) }
}

val storage = module {
    single { InternalStorageManager(androidContext()) }
}

fun getMoshi(): Moshi = Moshi.Builder().build()

fun getOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun getRetrofit(moshi: Moshi, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://move-scooters.herokuapp.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

fun getService(retrofit: Retrofit): ServiceApi =
    retrofit.create(ServiceApi::class.java)