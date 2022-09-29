package com.internship.move.utils.extensions

import android.widget.ImageView
import com.internship.move.R
import com.internship.move.utils.Constants.BatteryLevel.BATTERY_THRESHOLD_100
import com.internship.move.utils.Constants.BatteryLevel.BATTERY_THRESHOLD_80
import com.internship.move.utils.Constants.BatteryLevel.BATTERY_THRESHOLD_60
import com.internship.move.utils.Constants.BatteryLevel.BATTERY_THRESHOLD_40
import com.internship.move.utils.Constants.BatteryLevel.BATTERY_THRESHOLD_20

fun ImageView.setBatteryIcon(batteryLevel: Int) {
    this.setImageResource(
        when {
            batteryLevel >= BATTERY_THRESHOLD_100 -> R.drawable.ic_battery_100
            batteryLevel >= BATTERY_THRESHOLD_80 -> R.drawable.ic_battery_80
            batteryLevel >= BATTERY_THRESHOLD_60 -> R.drawable.ic_battery_60
            batteryLevel >= BATTERY_THRESHOLD_40 -> R.drawable.ic_battery_40
            batteryLevel >= BATTERY_THRESHOLD_20 -> R.drawable.ic_battery_20
            else -> R.drawable.ic_battery_no
        }
    )
}