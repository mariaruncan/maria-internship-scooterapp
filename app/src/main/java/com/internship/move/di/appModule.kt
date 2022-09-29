package com.internship.move.di

import com.internship.move.BuildConfig
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.network.ScooterApi
import com.internship.move.network.TripApi
import com.internship.move.network.UserApi
import com.internship.move.network.interceptors.TokenInterceptor
import com.internship.move.repository.ScooterRepository
import com.internship.move.repository.TripRepository
import com.internship.move.repository.UserRepository
import com.internship.move.ui.authentication.AuthenticationViewModel
import com.internship.move.ui.home.MainViewModel
import com.internship.move.ui.onboarding.OnboardingViewModel
import com.internship.move.ui.splash.SplashViewModel
import com.internship.move.utils.Constants.HttpClient.CONNECT_TIMEOUT
import com.internship.move.utils.Constants.HttpClient.READ_TIMEOUT
import com.internship.move.utils.Constants.HttpClient.WRITE_TIMEOUT
import com.internship.move.utils.InternalStorageManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import id.zelory.compressor.Compressor
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
    viewModel { AuthenticationViewModel(repo = get(), internalStorageManager = get(), errorJSONAdapter = get()) }
    viewModel {
        MainViewModel(
            userRepo = get(),
            scooterRepo = get(),
            tripRepo = get(),
            internalStorageManager = get(),
            errorJSONAdapter = get()
        )
    }
}

val repositories = module {
    single { UserRepository(api = get(), compressor = get(), context = androidContext()) }
    single { ScooterRepository(api = get()) }
    single { TripRepository(api = get()) }
}

val services = module {
    single { getFileCompressor() }
    single { getMoshi() }
    single { getTokenInterceptor(internalStorageManager = get()) }
    single { getOkHttpClient(tokenInterceptor = get()) }
    single { getRetrofit(moshi = get(), httpClient = get()) }
    single { getUserService(retrofit = get()) }
    single { getScooterService(retrofit = get()) }
    single { getErrorAdapter(moshi = get()) }
}

val storage = module {
    single { InternalStorageManager(androidContext()) }
}

private fun getErrorAdapter(moshi: Moshi): JsonAdapter<ErrorResponseDTO> = moshi.adapter(ErrorResponseDTO::class.java).lenient()

private fun getTokenInterceptor(internalStorageManager: InternalStorageManager) = TokenInterceptor(internalStorageManager)

private fun getFileCompressor(): Compressor = Compressor

private fun getMoshi(): Moshi = Moshi.Builder().build()

private fun getOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)
    }
    httpClient.addInterceptor(tokenInterceptor)

    return httpClient
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .build()
}

private fun getRetrofit(moshi: Moshi, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

private fun getUserService(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

private fun getScooterService(retrofit: Retrofit): ScooterApi = retrofit.create(ScooterApi::class.java)

private fun getTripService(retrofit: Retrofit): TripApi = retrofit.create(TripApi::class.java)
