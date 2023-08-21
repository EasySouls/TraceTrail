package dev.easysouls.tracetrail.presentation.map

import android.content.Context
import android.location.Location
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
import dev.easysouls.tracetrail.domain.util.toLatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinderViewModel @Inject constructor(
    private val repository: MissingPersonRepository,
    private val locationClient: LocationClient
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

//    fun onGetLocationUpdates(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            locationClient
//                .getLocationUpdates(LOCATION_UPDATES_INTERVAL)
//                .catch { e ->
//                    e.printStackTrace()
//                    if (e is)
//                }
//                .onEach { location ->
//                    location.toLatLng()
//                }
//        }
//    }

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
        private const val LOCATION_UPDATES_INTERVAL = 2000L
    }
}