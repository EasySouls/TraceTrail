package dev.easysouls.tracetrail.presentation.map

import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val isStyledMap: Boolean = false
)
