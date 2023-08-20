package dev.easysouls.tracetrail.presentation.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.easysouls.tracetrail.domain.location.LocationClient
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson
import dev.easysouls.tracetrail.domain.missing_person.repository.MissingPersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinderViewModel @Inject constructor(
    private val repository: MissingPersonRepository,
) : ViewModel() {

    var currentLocationState by mutableStateOf<LatLng?>(null)
        private set

    var mapState by mutableStateOf(MapState())
        private set


    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            repository.getMissingPersons().collectLatest { persons ->
                mapState = mapState.copy(
                    missingPersons = persons
                )
            }
        }
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleMapStyle -> {
                mapState = mapState.copy(
                    properties = mapState.properties.copy(
                        mapStyleOptions = if (mapState.isStyledMap) {
                            null
                        } else MapStyleOptions(MapStyle.json)
                    ),
                    isStyledMap = !mapState.isStyledMap
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
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    companion object {
        private const val TAG = "FinderViewModel"
    }
}