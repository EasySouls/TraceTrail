package dev.easysouls.tracetrail.data.local

import dev.easysouls.tracetrail.domain.missing_person.model.MissingPerson
import dev.easysouls.tracetrail.domain.missing_person.repository.MissingPersonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MissingPersonRepositoryImpl(
    private val dao: MissingPersonDao
): MissingPersonRepository {

    override suspend fun insertMissingPerson(person: MissingPerson) {
        dao.insertMissingPerson(person.toMissingPersonEntity())
    }

    override suspend fun deleteMissingPerson(person: MissingPerson) {
        dao.deleteMissingPerson(person.toMissingPersonEntity())
    }

    override fun getMissingPersons(): Flow<List<MissingPerson>> {
        return dao.getMissingPersons().map { persons ->
            persons.map { it.toMissingPerson() }
        }
    }
}