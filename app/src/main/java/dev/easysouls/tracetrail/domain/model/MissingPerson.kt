package dev.easysouls.tracetrail.domain.model

data class MissingPerson(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val lat: Double,
    val lng: Double,
    val uploadedBy: String = "Anonymous"
)
