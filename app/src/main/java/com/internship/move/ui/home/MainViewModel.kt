package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.model.Scooter
import com.internship.move.data.model.User
import com.internship.move.data.model.UserStatus
import com.internship.move.repository.ScooterRepository
import com.internship.move.repository.UserRepository
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.launch
import com.internship.move.data.model.UserStatus.FREE

class MainViewModel(
    private val userRepo: UserRepository,
    private val scooterRepo: ScooterRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    private var _status: UserStatus = FREE
    val status: UserStatus
        get() = _status

    private val _currentUser: MutableLiveData<User?> = MutableLiveData()
    val currentUser: LiveData<User?>
        get() = _currentUser

    private val _scootersList: MutableLiveData<List<Scooter>> = MutableLiveData(listOf())
    val scootersList: LiveData<List<Scooter>>
        get() = _scootersList

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val unlockSuccessful: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            try {
                _currentUser.value = userRepo.getCurrentUser().toUser()
            } catch (e: Exception) {
                _currentUser.value = null
                handleException(e)
            }
        }
    }

    fun logOut() {
        internalStorageManager.setToken(null)
        internalStorageManager.setHasDrivingLicense(false)
    }

    fun clearApp() {
        logOut()
        internalStorageManager.setHasSeenOnboarding(false)
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
                val user = response.user.toUser()
                _status = user.status
                user.scooter = response.scooter.toScooter()
                _currentUser.value = user
                unlockSuccessful.value = true
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun cancelScanScooter() {
        viewModelScope.launch {
            try {
                val number = _currentUser.value?.scooter?.number
                if (number != null) {
                    scooterRepo.cancelScanScooter(number)
                }
                _scootersList.value = scooterRepo.getAllScooters()
                _currentUser.value = userRepo.getCurrentUser().toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
    }
}
