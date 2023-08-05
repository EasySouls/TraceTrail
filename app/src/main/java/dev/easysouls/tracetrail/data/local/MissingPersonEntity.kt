package dev.easysouls.tracetrail.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MissingPersonEntity(
    val lat: Double,
    val lng: Double,
    @PrimaryKey val id: Int? = null
)
