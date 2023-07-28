package dev.easysouls.tracetrail.data

import com.google.android.gms.maps.model.LatLng

data class MissingPerson(
    val firstName: String,
    val lastName: String,
    val lastKnownLocation: LatLng,
    val images: List<String>?,
    val uploadedBy: String = "Unknown"
)
