package dev.easysouls.tracetrail.presentation.map

import com.google.android.gms.maps.model.LatLng
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson

sealed class MapEvent {
    data object ToggleMapStyle: MapEvent()
    data class OnMapLongClick(val latLng: LatLng): MapEvent()
    data class OnInfoWindowLongClick(val person: MissingPerson): MapEvent()
}
