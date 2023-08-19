package dev.easysouls.tracetrail.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.Builder
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dev.easysouls.tracetrail.domain.PermissionHandler
import dev.easysouls.tracetrail.domain.location.LocationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val permissionHandler: PermissionHandler,
    private val application: Application
) : LocationTracker {

    private val locationFlow = MutableStateFlow<Location?>(null)
    private var locationCallback: LocationCallback? = null

    init {
        startLocationUpdates()
    }

    override suspend fun getLastLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            permissionHandler.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            permissionHandler.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION) 
        }

        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result)
                    } else {
                        cont.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it)
                }
                addOnFailureListener {
                    cont.resume(null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let { location ->
                    locationFlow.value = location
                }
            }
        }

        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val locationRequest = Builder(
            priority, 1000
        ).build()

        if (
            permissionHandler.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            permissionHandler.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            locationCallback?.let {
                locationClient.requestLocationUpdates(locationRequest, it, null)
            }
        } else {
            permissionHandler.requestPermission(Manifest.permission_group.LOCATION)
        }
    }


    override fun getLocationUpdates(): Flow<Location> {
        return locationFlow.filterNotNull()
    }

    override fun stopLocationUpdates() {
        locationCallback?.let {
            locationClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }

}
