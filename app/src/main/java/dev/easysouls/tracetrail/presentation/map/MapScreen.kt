package dev.easysouls.tracetrail.presentation.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import dev.easysouls.tracetrail.presentation.NavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    viewModel: MapViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    val scope = rememberCoroutineScope()

    val budapest = LatLng(47.526642, 19.046394)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(budapest, 10f)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(MapEvent.ToggleMapStyle)
            }) {
                Icon(
                    imageVector = if (viewModel.state.isStyledMap) {
                        Icons.Default.KeyboardArrowRight
                    } else Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Toggle Fallout map"
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NavigationBar(navController = navController)
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
                
            }
        )
    }
}