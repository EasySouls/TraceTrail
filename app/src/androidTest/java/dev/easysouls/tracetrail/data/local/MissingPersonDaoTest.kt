package dev.easysouls.tracetrail.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MissingPersonDaoTest {

    private lateinit var db: MissingPersonDatabase
    private lateinit var dao: MissingPersonDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MissingPersonDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.dao
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertMissingPerson() = runTest {
        val person = MissingPersonEntity(2, "James", "Whitney", 32.0, 43.0, "User1")

        dao.insertMissingPerson(person)

        val missingPersons = dao.getMissingPersons().first().first()
        assertThat(missingPersons).isEqualTo(person)
    }

    @Test
    fun insertAndRetrieveMissingPersons() = runTest {
        val person1 = MissingPersonEntity(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            lat = 40.7128,
            lng = -74.0060,
            uploadedBy = "User1"
        )
        val person2 = MissingPersonEntity(
            id = 2,
            firstName = "Jane",
            lastName = "Smith",
            lat = 34.0522,
            lng = -118.2437,
            uploadedBy = "User2"
        )

        dao.insertMissingPerson(person1)
        dao.insertMissingPerson(person2)

        val missingPersons = dao.getMissingPersons().first()
        assertEquals(2, missingPersons.size)
        assertEquals(person1, missingPersons[0])
        assertEquals(person2, missingPersons[1])
    }

    @Test
    fun insertAndDeleteMissingPerson() = runTest {
        val person = MissingPersonEntity(
            id = 1,
            firstName = "Alice",
            lastName = "Johnson",
            lat = 51.5074,
            lng = -0.1278,
            uploadedBy = "User3"
        )

        dao.insertMissingPerson(person)
        val insertedPerson = dao.getMissingPersons().first().first()
        assertThat(person).isEqualTo(insertedPerson)

        dao.deleteMissingPerson(insertedPerson)
        val missingPersonsAfterDelete = dao.getMissingPersons().first()
        assertThat(missingPersonsAfterDelete).isEmpty()
    }
}
