package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.model.Trip
import com.internship.move.repository.TripRepository
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TripViewModel(
    private val tripRepo: TripRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    val trip: MutableLiveData<Trip?> = MutableLiveData(null)

    private val _tripsList: MutableLiveData<List<Trip>> = MutableLiveData()
    val tripsList: LiveData<List<Trip>>
        get() = _tripsList

    private val _seconds: MutableLiveData<Int> = MutableLiveData(0)
    val seconds: LiveData<Int>
        get() = _seconds

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var tripJob: Job? = null
    private var timeJob: Job? = null

    fun startRide(scooterId: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                tripRepo.startRide(scooterId, latitude, longitude)
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
                _seconds.value = 0
                trip.value = tripRepo.endRide(scooterId, latitude, longitude)
                stopTimeUpdates()
                stopTripUpdates()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun lockRide(scooterId: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                tripRepo.lockRide(scooterId, latitude, longitude)
                stopTimeUpdates()
                stopTripUpdates()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun unlockRide(scooterId: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                tripRepo.unlockRide(scooterId, latitude, longitude)
                startTimeUpdates()
                startTripUpdates()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getCurrentTrip() {
        viewModelScope.launch {
            try {
                val scooterId = internalStorageManager.getScooterId() ?: throw Exception("scooterId is null!!")
                trip.value = tripRepo.getCurrentTrip(scooterId)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getUserTrips() {
        viewModelScope.launch {
            try {
                _tripsList.value = tripRepo.getUserTrips()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun startTimeUpdates() {
        stopTimeUpdates()
        timeJob = viewModelScope.launch {
            while (true) {
                _seconds.value = _seconds.value?.plus(1)
                delay(TIME_UPDATES_INTERVAL)
            }
        }
    }

    private fun stopTimeUpdates() {
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
        trip.value = null
        tripJob?.cancel()
        tripJob = null
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
    }

    companion object {
        private const val TIME_UPDATES_INTERVAL = 1000L
        private const val TRIP_UPDATES_INTERVAL = 10000L
    }
}