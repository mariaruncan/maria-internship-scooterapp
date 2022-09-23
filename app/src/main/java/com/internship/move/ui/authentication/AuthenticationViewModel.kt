package com.internship.move.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.model.User
import com.internship.move.repository.UserRepository
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val repo: UserRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val user: MutableLiveData<User> = MutableLiveData()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repo.login(email, password)
                internalStorageManager.setToken(response.token)
                internalStorageManager.setHasDrivingLicense(response.user.drivingLicense != null)
                isLoading.value = false
                user.value = response.user.toUser()
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
                user.value = response.user.toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun addDrivingLicense(imagePath: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repo.addLicense(imagePath)
                internalStorageManager.setHasDrivingLicense(true)
                isLoading.value = false
                user.value = response.user.toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun resetPassword(newPassword: String) {
        // TODO()
    }

    private fun handleException(e: Exception) {
        val x = e.toErrorResponseDTO(errorJSONAdapter)
        errorMessage.value = x.message
        isLoading.value = false
    }
}
