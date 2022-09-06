package com.internship.move.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.internship.move.network.model.AddLicenseResponse
import com.internship.move.network.model.ErrorResponse
import com.internship.move.network.model.LoginResponse
import com.internship.move.network.model.RegisterResponse
import com.internship.move.repository.UserRepository
import com.internship.move.utils.Constants.LOADING_DELAY
import com.internship.move.utils.InternalStorageManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.delay
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException

class AuthenticationViewModel(
    private val repo: UserRepository,
    private val internalStorageManager: InternalStorageManager
) : ViewModel() {

    private val moshi: Moshi by inject(Moshi::class.java)
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun register(name: String, email: String, password: String): RegisterResponse? =
        try {
            isLoading.value = true
            delay(LOADING_DELAY)
            val response = repo.register(name, email, password)
            internalStorageManager.setToken(response.token)
            response
        } catch (e: Exception) {
            handleException(e)
            null
        }

    suspend fun addDrivingLicense(imagePath: String): AddLicenseResponse? =
        try {
            isLoading.value = true
            delay(LOADING_DELAY)
            val response = repo.addLicense(internalStorageManager.getToken() ?: "", imagePath)
            internalStorageManager.setHasDrivingLicense(true)
            response
        } catch (e: Exception) {
            handleException(e)
            null
        }

    suspend fun login(email: String, password: String): LoginResponse? =
        try {
            isLoading.value = true
            delay(LOADING_DELAY)
            val response = repo.login(email, password)
            internalStorageManager.setToken(response.token)
            response
        } catch (e: Exception) {
            handleException(e)
            null
        }

    fun resetPassword(newPassword: String) {
        // api call
    }

    private fun handleException(e: Exception) {
        when (e) {
            is HttpException -> {
                val errorResponse = e.response()?.errorBody()?.source()?.let { moshi.adapter(ErrorResponse::class.java).fromJson(it) }
                errorMessage.value = errorResponse?.message
            }
            else -> errorMessage.value = e.message
        }
        isLoading.value = false
    }
}