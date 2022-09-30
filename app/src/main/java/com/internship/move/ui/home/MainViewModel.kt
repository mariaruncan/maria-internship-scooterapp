package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.model.Scooter
import com.internship.move.data.model.Trip
import com.internship.move.data.model.User
import com.internship.move.repository.ScooterRepository
import com.internship.move.repository.TripRepository
import com.internship.move.repository.UserRepository
import com.internship.move.ui.home.map.MapFragment
import com.internship.move.ui.home.unlock.UnlockFragment
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepo: UserRepository,
    private val scooterRepo: ScooterRepository,
    private val tripRepo: TripRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    private val _currentUser: MutableLiveData<User?> = MutableLiveData()
    val currentUser: LiveData<User?>
        get() = _currentUser

    private val _scootersList: MutableLiveData<List<Scooter>> = MutableLiveData(listOf())
    val scootersList: LiveData<List<Scooter>>
        get() = _scootersList

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val unlockResult: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _seconds: MutableLiveData<Int> = MutableLiveData(0)
    val seconds: LiveData<Int>
        get() = _seconds

    private val _trip: MutableLiveData<Trip?> = MutableLiveData(null)
    val trip: LiveData<Trip?>
        get() = _trip

    private var timeJob: Job? = null
    private var tripJob: Job? = null

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val scooter = _currentUser.value?.scooter
                _currentUser.value = userRepo.getCurrentUser().toUser().copy(scooter = scooter)
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

    fun beepScooter(scooterId: String) {
        viewModelScope.launch {
            try {
                scooterRepo.beepScooter(scooterId)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun scanScooter(method: UnlockMethod, scooterId: Int, location: LatLng) {
        viewModelScope.launch {
            try {
                val response = scooterRepo.scanScooter(method, scooterId, location)
                val scooter = response.scooter.toScooter()
                val user = response.user.toUser().copy(numberOfTrips = _currentUser.value?.numberOfTrips, scooter = scooter)
                internalStorageManager.setScooterId(scooter.id)
                unlockResult.value = true
                delay(UNLOCK_SUCCESSFUL_DELAY)
                _currentUser.value = user
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
                    internalStorageManager.setScooterId(null)
                }
                _currentUser.value = userRepo.getCurrentUser().toUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun startRide(scooterNumber: Int, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                tripRepo.startRide(scooterNumber, latitude, longitude)
                getCurrentUser()
                startTimeUpdates()
                startTripUpdates()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun endRide(scooterId: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                tripRepo.endRide(scooterId, latitude, longitude)
                internalStorageManager.setScooterId(null)
                stopTimeUpdates()
                stopTripUpdates()
                getCurrentUser()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getCurrentTrip() {
        viewModelScope.launch {
            try {
                val scooterId = internalStorageManager.getScooterId() ?: throw Exception("not in a trip")
                _trip.value = tripRepo.getCurrentTrip(scooterId)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getUserTrips() {
        viewModelScope.launch {
            try {
                val tripsList = tripRepo.getUserTrips()
                // set live data
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun startTimeUpdates() {
        stopTimeUpdates()
        _seconds.value = 0
        timeJob = viewModelScope.launch {
            while (true) {
                _seconds.value = _seconds.value?.plus(1)
                delay(TIME_UPDATES_INTERVAL)
            }
        }
    }

    private fun stopTimeUpdates() {
        _seconds.value = 0
        timeJob?.cancel()
        timeJob = null
    }

    private fun startTripUpdates() {
        stopTripUpdates()
        tripJob = viewModelScope.launch {
            while (true) {
                getCurrentTrip()
                delay(TRIP_UPDATES_INTERVAL)
            }
        }
    }

    private fun stopTripUpdates() {
        _trip.value = null
        tripJob?.cancel()
        tripJob = null
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
    }

    companion object {
        private const val UNLOCK_SUCCESSFUL_DELAY = 2000L
        private const val TIME_UPDATES_INTERVAL = 1000L
        private const val TRIP_UPDATES_INTERVAL = 10000L
    }
}
