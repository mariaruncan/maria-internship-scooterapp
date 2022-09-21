package com.internship.move.utils.extensions

import android.widget.ImageView
import com.internship.move.R

fun ImageView.setBatteryIcon(batteryLevel: Int) {
    this.setImageResource(
        when {
            batteryLevel >= 100 -> R.drawable.ic_battery_100
            batteryLevel >= 80 -> R.drawable.ic_battery_80
            batteryLevel >= 60 -> R.drawable.ic_battery_60
            batteryLevel >= 40 -> R.drawable.ic_battery_40
            batteryLevel >= 20 -> R.drawable.ic_battery_20
            else -> R.drawable.ic_battery_no
        }
    )
}