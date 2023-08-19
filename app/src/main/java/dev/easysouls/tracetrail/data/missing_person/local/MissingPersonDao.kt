package dev.easysouls.tracetrail.data.missing_person.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MissingPersonDao {

    @Upsert
    suspend fun insertMissingPerson(person: MissingPersonEntity)

    @Delete
    suspend fun deleteMissingPerson(person: MissingPersonEntity)

    @Query("SELECT * FROM missing_people")
    fun getMissingPersons(): Flow<List<MissingPersonEntity>>
}