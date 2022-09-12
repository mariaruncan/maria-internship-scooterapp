package com.internship.move.ui.home.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.internship.move.R
import com.internship.move.data.model.Scooter
import com.internship.move.databinding.ViewScooterInfoBinding

class ScooterInfoWindowAdapter(
    context: Context
) : GoogleMap.InfoWindowAdapter {

    private val binding = ViewScooterInfoBinding.inflate(LayoutInflater.from(context))

    private fun displayData(scooter: Scooter) {
        binding.scooterNumberTV.text = "#${scooter.number}"
        binding.batteryIV.setImageResource(
            when {
                scooter.batteryLevel >= 100 -> R.drawable.ic_battery_100
                scooter.batteryLevel >= 80 -> R.drawable.ic_battery_80
                scooter.batteryLevel >= 60 -> R.drawable.ic_battery_60
                scooter.batteryLevel >= 40 -> R.drawable.ic_battery_40
                scooter.batteryLevel >= 20 -> R.drawable.ic_battery_20
                else -> R.drawable.ic_battery_no
            }
        )
        binding.batteryTV.text = "${scooter.batteryLevel}%"
        binding.addressTV.text = scooter.address
    }

    override fun getInfoContents(marker: Marker): View? {
        val scooter = marker.tag as? Scooter ?: return null
        displayData(scooter)
        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? {
        val scooter = marker.tag as? Scooter ?: return null
        displayData(scooter)
        return binding.root
    }
}