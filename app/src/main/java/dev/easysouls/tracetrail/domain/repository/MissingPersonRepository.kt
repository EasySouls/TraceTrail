package dev.easysouls.tracetrail.domain.repository

import dev.easysouls.tracetrail.domain.model.MissingPerson
import kotlinx.coroutines.flow.Flow

interface MissingPersonRepository {

    suspend fun insertMissingPerson(person: MissingPerson)
    suspend fun deleteMissingPerson(person: MissingPerson)
    fun getMissingPersons(): Flow<List<MissingPerson>>

}