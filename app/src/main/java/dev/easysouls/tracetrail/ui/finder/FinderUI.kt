package dev.easysouls.tracetrail.ui.finder

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import dev.easysouls.tracetrail.R
import dev.easysouls.tracetrail.data.MissingPerson

@Composable
fun FinderUI() {

}

@Composable
fun MissingPersonCard(
    person: MissingPerson,
    context: Context
) {
    var isLoading by remember { mutableStateOf(true) }

    // Load data from Firestore

    Card {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .border(BorderStroke(5.dp, MaterialTheme.colorScheme.inversePrimary))
        ) {
            var expanded by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.clickable {expanded = !expanded}
            ) {
                Image(painter = painterResource(R.drawable.ic_headphone))
                AnimatedVisibility(expanded) {
                    Text(
                        text = "Random cucc",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}