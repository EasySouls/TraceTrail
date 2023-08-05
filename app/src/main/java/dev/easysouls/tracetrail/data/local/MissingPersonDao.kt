package dev.easysouls.tracetrail.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MissingPersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissingPerson(person: MissingPersonEntity)

    @Delete
    suspend fun deleteMissingPerson(person: MissingPersonEntity)

    @Query("SELECT * FROM missingpersonentity")
    fun getMissingPersons(): Flow<List<MissingPersonEntity>>
}