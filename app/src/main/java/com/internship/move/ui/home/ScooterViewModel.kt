package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.dto.ErrorResponseDTO
import com.internship.move.data.model.Scooter
import com.internship.move.repository.ScooterRepository
import com.internship.move.ui.home.unlock.UnlockMethod
import com.internship.move.utils.InternalStorageManager
import com.internship.move.utils.extensions.toErrorResponseDTO
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScooterViewModel(
    private val scooterRepo: ScooterRepository,
    private val internalStorageManager: InternalStorageManager,
    private val errorJSONAdapter: JsonAdapter<ErrorResponseDTO>
) : ViewModel() {

    private val _scootersList: MutableLiveData<List<Scooter>> = MutableLiveData(listOf())
    val scootersList: LiveData<List<Scooter>>
        get() = _scootersList

    private val _scannedScooter: MutableLiveData<Scooter?> = MutableLiveData(null)
    val scannedScooter: LiveData<Scooter?>
        get() = _scannedScooter

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val unlockResult: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getAllScooters() {
        viewModelScope.launch {
            try {
                _scootersList.value = scooterRepo.getAllScooters()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun ringScooter(scooterId: String) {
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
                unlockResult.value = true
                internalStorageManager.setScooterId(scooter.id)
                internalStorageManager.setScooterNumber(scooter.number)
                delay(UNLOCK_SUCCESSFUL_DELAY)
                _scannedScooter.value = scooter
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun cancelScanScooter(scooterNumber: Int) {
        viewModelScope.launch {
            try {
                scooterRepo.cancelScanScooter(scooterNumber)
                internalStorageManager.setScooterId(null)
                internalStorageManager.setScooterNumber(0)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleException(e: Exception) {
        _errorMessage.value = e.toErrorResponseDTO(errorJSONAdapter).message
    }

    companion object {
        private const val UNLOCK_SUCCESSFUL_DELAY = 2000L
    }
}