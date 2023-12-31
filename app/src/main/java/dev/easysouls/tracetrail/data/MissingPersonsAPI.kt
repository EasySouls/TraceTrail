package dev.easysouls.tracetrail.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MissingPersonsAPI {

    private val db = Firebase.firestore
    private val missingPersonsCollectionRef = db.collection("missingPersons")

    fun saveMissingPerson(person: MissingPerson, context: Context) = CoroutineScope(Dispatchers.IO).launch {
        try {
            missingPersonsCollectionRef.add(person).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Successfully saved ${person.firstName}'s data",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        private const val TAG = "MissingPersonViewModel"
    }
}