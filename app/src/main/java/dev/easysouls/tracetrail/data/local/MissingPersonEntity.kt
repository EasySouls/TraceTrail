package dev.easysouls.tracetrail.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MissingPersonEntity(
    @PrimaryKey val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val lat: Double,
    val lng: Double,
    val uploadedBy: String = "Anonymous"
)
