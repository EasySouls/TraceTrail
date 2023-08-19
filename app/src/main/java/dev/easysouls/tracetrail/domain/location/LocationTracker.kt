package dev.easysouls.tracetrail.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker {
    suspend fun getLastLocation(): Location?
    fun getLocationUpdates(): Flow<Location>
    fun startLocationUpdates()
    fun stopLocationUpdates()
}