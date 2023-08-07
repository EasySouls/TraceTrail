package dev.easysouls.tracetrail.domain.missing_person.model

data class MissingPerson(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val lat: Double,
    val lng: Double,
    val uploadedBy: String = "Anonymous"
)
