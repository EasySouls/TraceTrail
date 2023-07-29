package dev.easysouls.tracetrail.data

import com.google.android.gms.maps.model.LatLng

data class MissingPerson(
    val firstName: String,
    val lastName: String,
    val lastKnownLocation: String,
    val image: String,
    val uploadedBy: String = "Unknown"
)
