package com.internship.move.ui.authentication

import androidx.lifecycle.LiveData
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

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User>
        get() = _user

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repo.login(email, password)
                internalStorageManager.setToken(response.token)
                internalStorageManager.setHasDrivingLicense(response.user.drivingLicense != null)
                _isLoading.value = false
                _user.value = response.user.toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repo.register(name, email, password)
                internalStorageManager.setToken(response.token)
                _isLoading.value = false
                _user.value = response.user.toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun addDrivingLicense(imagePath: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repo.addLicense(imagePath)
                internalStorageManager.setHasDrivingLicense(true)
                _isLoading.value = false
                _user.value = response.user.toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun resetPassword(newPassword: String) {
        // TODO()
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
        _isLoading.value = false
    }
}
