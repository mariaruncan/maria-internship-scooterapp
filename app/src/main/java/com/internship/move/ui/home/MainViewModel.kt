package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.dto.UserDTO
import com.internship.move.data.model.Scooter
import com.internship.move.repository.ScooterRepository
import com.internship.move.repository.UserRepository
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepo: UserRepository,
    private val scooterRepo: ScooterRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    private val _currentUser: MutableLiveData<UserDTO?> = MutableLiveData()
    val currentUser: LiveData<UserDTO?>
        get() = _currentUser

    private val _scootersList: MutableLiveData<List<Scooter>> = MutableLiveData(listOf())
    val scootersList: LiveData<List<Scooter>>
        get() = _scootersList

    private var _selectedScooter: MutableLiveData<Scooter?> = MutableLiveData()
    val selectedScooter: LiveData<Scooter?>
        get() = _selectedScooter

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun logOut() {
        internalStorageManager.setToken(null)
        internalStorageManager.setHasDrivingLicense(false)
    }

    fun clearApp() {
        logOut()
        internalStorageManager.setHasSeenOnboarding(false)
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepo.getCurrentUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getAllScooters() {
        viewModelScope.launch {
            try {
                _scootersList.value = scooterRepo.getAllScooters()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun scanScooter(method: UnlockMethod, scooterId: Int, location: LatLng) {
        viewModelScope.launch {
            try {
                val response = scooterRepo.scanScooter(method, scooterId, location)
                _selectedScooter.value = response.scooter.toScooter()
                _currentUser.value = response.user
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun cancelScanScooter() {
        viewModelScope.launch {
            try {
                _selectedScooter.value?.number?.let { scooterRepo.cancelScanScooter(it) }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
    }
}
