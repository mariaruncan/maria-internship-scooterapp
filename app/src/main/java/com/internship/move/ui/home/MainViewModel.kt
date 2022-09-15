package com.internship.move.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.internship.move.data.model.Scooter
import com.internship.move.repository.ScooterRepository
import com.internship.move.utils.InternalStorageManager
import kotlinx.coroutines.launch

class MainViewModel(
    private val repo: ScooterRepository,
    private val internalStorageManager: InternalStorageManager
) : ViewModel() {

    private val _scootersList: MutableLiveData<List<Scooter>> = MutableLiveData(listOf())
    val scootersList: LiveData<List<Scooter>>
        get() = _scootersList

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
                _scootersList.value = repo.getAllScooters()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
