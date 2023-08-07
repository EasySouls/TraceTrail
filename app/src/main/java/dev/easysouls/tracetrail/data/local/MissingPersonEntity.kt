package dev.easysouls.tracetrail.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missing_people")
data class MissingPersonEntity(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lng: Double,
    @ColumnInfo(name = "uploaded_by") val uploadedBy: String = "Anonymous"
)
