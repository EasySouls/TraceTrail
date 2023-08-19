package dev.easysouls.tracetrail.presentation.finder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson

@Composable
fun MissingPersonCard(
    person: MissingPerson,
    onInfoClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Image(painter = person.image, contentDescription = "Image of the missing person")
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = "${person.firstName} ${person.lastName}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Last seen at: ${person.lat}, ${person.lng}",
                    fontSize = 16.sp
                )
            }
            IconButton(onClick = { onInfoClick }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info about the missing person")
            }
            IconButton(onClick = { onSearchClick }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Info about the missing person")
            }
        }
    }
}

@Preview
@Composable
fun MissingPersonCardPreview() {
    MissingPersonCard(
        person = MissingPerson(
            1,
            "Jonathan",
            "O'Brian",
            45.32,
            77.54,
            "kislaszlo47@gmail.com"
        ),
        onInfoClick = {},
        onSearchClick = {}
    )
}