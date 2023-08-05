package dev.easysouls.tracetrail.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MapStyleOptions

class MapViewModel: ViewModel() {

    var state by mutableStateOf(MapState())

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleMapStyle -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isStyledMap) {
                            null
                        } else MapStyleOptions(MapStyle.json)
                    ),
                    isStyledMap = !state.isStyledMap
                )
            }
        }
    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted &&! visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}