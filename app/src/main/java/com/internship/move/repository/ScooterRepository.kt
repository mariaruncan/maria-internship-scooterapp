package com.internship.move.repository

import com.internship.move.data.model.Scooter
import com.internship.move.network.ScooterApi

class ScooterRepository(
    private val api: ScooterApi
) {

    suspend fun getAllScooters(): List<Scooter> = api.getAllScooters().scooters.map { scooterDTO -> scooterDTO.toScooter() }
}
