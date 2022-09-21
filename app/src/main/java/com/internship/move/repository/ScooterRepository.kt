package com.internship.move.repository

import com.google.android.gms.maps.model.LatLng
import com.internship.move.data.dto.ScanResponseDTO
import com.internship.move.data.model.Scooter
import com.internship.move.network.ScooterApi
import com.internship.move.ui.home.unlock.UnlockMethod

class ScooterRepository(
    private val api: ScooterApi
) {

    suspend fun getAllScooters(): List<Scooter> = api.getAllScooters().scooters.map { scooterDTO -> scooterDTO.toScooter() }

    suspend fun scanScooter(method: UnlockMethod, id: Int, location: LatLng): ScanResponseDTO =
        api.scanScooter(method.name, id, location.latitude, location.longitude)

    suspend fun cancelScanScooter(id: Int): Unit = api.cancelScanScooter(id)
}
