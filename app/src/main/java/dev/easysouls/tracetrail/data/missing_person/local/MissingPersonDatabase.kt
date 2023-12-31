package dev.easysouls.tracetrail.data.missing_person.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MissingPersonEntity::class],
    version = 1
)
abstract class MissingPersonDatabase: RoomDatabase() {

    abstract val dao: MissingPersonDao
}