package dev.easysouls.tracetrail.presentation.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.easysouls.tracetrail.R

@Composable
fun NavigationBar() {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavBarItem(src = R.drawable.ic_launcher_background, name = "1")
        Spacer(modifier = Modifier.width(10.dp))
        NavBarItem(src = R.drawable.ic_launcher_foreground, name = "2")
        NavBarItem(src = R.drawable.ic_launcher_background, name = "3")
    }
}

@Composable
fun NavBarItem(src: Int, name: String) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(src),
            contentDescription = "$name navbar icon",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary)),
            contentScale = ContentScale.Crop
        )
    }
}