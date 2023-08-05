package dev.easysouls.tracetrail.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MissingPersonDao {

    // Apparently you can't use suspend functions with room queries :(

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMissingPerson(person: MissingPersonEntity)

    @Delete
    fun deleteMissingPerson(person: MissingPersonEntity)

    @Query("SELECT * FROM missingpersonentity")
    fun getMissingPersons(): Flow<List<MissingPersonEntity>>
}