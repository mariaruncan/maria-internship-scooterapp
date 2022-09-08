package com.internship.move.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.dto.UserDTO
import com.internship.move.repository.UserRepository
import com.internship.move.utils.InternalStorageManager
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthenticationViewModel(
    private val repo: UserRepository,
    private val internalStorageManager: InternalStorageManager,
    private val moshi: Moshi
) : ViewModel() {

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val user: MutableLiveData<UserDTO> = MutableLiveData()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repo.login(email, password)
                internalStorageManager.setToken(response.token)
                internalStorageManager.setHasDrivingLicense(response.user.drivingLicense != null)
                isLoading.value = false
                user.value = response.user
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repo.register(name, email, password)
                internalStorageManager.setToken(response.token)
                isLoading.value = false
                user.value = response.user
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun addDrivingLicense(imagePath: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repo.addLicense(internalStorageManager.getToken() ?: "", imagePath)
                internalStorageManager.setHasDrivingLicense(true)
                isLoading.value = false
                user.value = response.user
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun resetPassword(newPassword: String) {
        // TODO()
    }

    private fun handleException(e: Exception) {
        when (e) {
            is HttpException -> {
                val errorResponse = e.response()?.errorBody()?.source()?.let { moshi.adapter(ErrorResponseDTO::class.java).fromJson(it) }
                errorMessage.value = errorResponse?.message
            }
            else -> errorMessage.value = e.message
        }
        isLoading.value = false
    }
}
