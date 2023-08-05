package dev.easysouls.tracetrail.presentation.map

sealed class MapEvent {
    data object ToggleMapStyle: MapEvent()
}
