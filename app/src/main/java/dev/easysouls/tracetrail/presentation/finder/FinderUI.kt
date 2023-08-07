package dev.easysouls.tracetrail.presentation.finder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.easysouls.tracetrail.R
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson

@Composable
fun FinderUI(persons: List<MissingPerson>?) {
    LazyColumn {
        item {
            Text("Missing Persons")
        }

        if (persons != null) {
            items(persons) {person ->
                MissingPersonCard(person = person)
            }
        }
    }
}

@Composable
fun MissingPersonCard(
    person: MissingPerson
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary)
                .border(BorderStroke(1.dp, Color.Black))
        ) {
            var expanded by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Image(painter = painterResource(R.drawable.ic_headphone), contentDescription = "Image of ${person.firstName}")
                AnimatedVisibility(expanded) {
                    Text(
                        text = person.firstName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun NewMissingPerson() {
    var firstName = remember { mutableStateOf("") }
    var lastName = remember { mutableStateOf("") }
    var lastKnownLocation = remember { mutableStateOf("") }
    var image = remember { mutableStateOf("") }
    var uploadedBy = remember { mutableStateOf(Firebase.auth.currentUser?.email) }

    Text(text = "Add a new missing person: ")
}
