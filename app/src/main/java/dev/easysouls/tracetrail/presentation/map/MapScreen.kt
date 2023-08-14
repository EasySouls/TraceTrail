package dev.easysouls.tracetrail.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    viewModel: MapViewModel
) {
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }

    val budapest = LatLng(47.526642, 19.046394)
    val cameraPositionState = rememberCameraPositionState {
        position = if (viewModel.currentLocation != null) {
            CameraPosition.fromLatLngZoom(viewModel.currentLocation!!, 10f)
        } else {
            CameraPosition.fromLatLngZoom(budapest, 10f)
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(MapEvent.ToggleMapStyle)
                },
                ) {
                Icon(
                    imageVector = if (viewModel.state.isStyledMap) {
                        Icons.Default.KeyboardArrowRight
                    } else Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Toggle Fallout map"
                )
            }
        }
    ) { values ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            cameraPositionState = cameraPositionState,
            properties = viewModel.state.properties,
            uiSettings = uiSettings,
            onMapLongClick = {
                viewModel.onEvent(MapEvent.OnMapLongClick(it))
            }
        ) {
            viewModel.state.missingPersons.forEach { person ->
                Marker(
                    state = MarkerState(LatLng(person.lat, person.lng)),
                    title = "Missing Person (${person.lat}, ${person.lng})",
                    snippet = "Name: ${person.firstName} ${person.lastName}, Uploaded by: ${person.uploadedBy}",
                    onClick = {
                        it.showInfoWindow()
                        true
                    },
                    onInfoWindowLongClick = {
                        viewModel.onEvent(
                            MapEvent.OnInfoWindowLongClick(person)
                        )
                    },
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_ORANGE
                    )
                )
            }
        }
    }
}