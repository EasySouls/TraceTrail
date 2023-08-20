package dev.easysouls.tracetrail.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    suspend fun getLastLocation(): Location?
    fun getLocationUpdates(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}