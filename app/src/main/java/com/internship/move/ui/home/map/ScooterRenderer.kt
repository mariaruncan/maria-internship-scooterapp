package com.internship.move.ui.home.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.internship.move.R
import com.internship.move.data.model.Scooter
import com.internship.move.utils.extensions.getDrawableToBitmapDescriptor

class ScooterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<Scooter>
) : DefaultClusterRenderer<Scooter>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: Scooter, markerOptions: MarkerOptions) {
        markerOptions.position(item.latLng).icon(context.getDrawableToBitmapDescriptor(R.drawable.ic_scooter))
    }

    override fun onClusterItemRendered(clusterItem: Scooter, marker: Marker) {
        marker.tag = clusterItem
    }

    override fun getColor(clusterSize: Int): Int = context.getColor(R.color.indigo)
}