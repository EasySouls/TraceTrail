package dev.easysouls.tracetrail.data

data class MissingPerson(
    val firstName: String,
    val lastName: String,
    val lastKnownLocation: String,
    val image: String,
    val uploadedBy: String = "Unknown"
)
