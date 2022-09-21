package com.internship.move.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Scooter(
    val id: String,
    val number: Int,
    val latLng: LatLng,
    val batteryLevel: Int,
    val bookedStatus: String,
    val lockedStatus: String,
    var address: String = ""
) : ClusterItem {

    override fun getPosition(): LatLng = latLng

    override fun getTitle(): String = id

    override fun getSnippet(): String = address
}
