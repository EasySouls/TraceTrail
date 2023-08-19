package dev.easysouls.tracetrail.presentation.finder

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson

@Composable
fun FinderUI(persons: List<MissingPerson>?) {
    LazyColumn {
        item {
            Text("Missing Persons")
        }

        if (persons != null) {
            items(persons) {person ->
                MissingPersonCard(person = person, { TODO()}, { TODO()})
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
    val uploadedBy = remember { mutableStateOf(Firebase.auth.currentUser?.email) }

    Text(text = "Add a new missing person: ")
}
