package dev.easysouls.tracetrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import dev.easysouls.tracetrail.ui.theme.TraceTrailTheme

private const val MAPS_API_KEY = BuildConfig.MAPS_API_KEY

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TraceTrailTheme {
                Map()
            }
        }
    }
}

@Composable
fun Map() {
    val budapest = LatLng(47.497913, 19.040236)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(budapest, 12f)
    }
    val mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }
    val mapUiSettings by remember { mutableStateOf(MapUiSettings()) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = mapUiSettings
    ) {
        Marker(
            state = MarkerState(position = budapest),
            title = "Budapest",
            snippet = "Marker in Budapest"
        )
    }
}
