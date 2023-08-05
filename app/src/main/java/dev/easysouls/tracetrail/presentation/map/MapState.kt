package dev.easysouls.tracetrail.presentation.map

import com.google.maps.android.compose.MapProperties
import dev.easysouls.tracetrail.domain.model.MissingPerson

data class MapState(
    val properties: MapProperties = MapProperties(),
    val missingPersons: List<MissingPerson> = emptyList(),
    val isStyledMap: Boolean = false
)
