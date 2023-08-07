package dev.easysouls.tracetrail.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson
import dev.easysouls.tracetrail.domain.missing_person.repository.MissingPersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: MissingPersonRepository
): ViewModel() {

    var currentLocation by mutableStateOf<LatLng?>(null)

    var state by mutableStateOf(MapState())

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            repository.getMissingPersons().collectLatest { persons ->
                state = state.copy(
                    missingPersons = persons
                )
            }
        }
    }

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
            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.deleteMissingPerson(event.person)
                }
            }
            is MapEvent.OnMapLongClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insertMissingPerson(
                        MissingPerson(
                            firstName = "",
                            lastName = "",
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude
                        )
                    )
                }
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