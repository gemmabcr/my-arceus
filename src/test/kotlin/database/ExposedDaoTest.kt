package database

import dev.gemmabcr.database.ExposedDao
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.models.Area
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.Type
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExposedDaoTest {
    private val dao = ExposedDao()

    @BeforeTest
    fun setup() {
        TestDatabaseFactory.init()
        insertTestData()
    }

    @AfterTest
    fun tearDown() {
        TestDatabaseFactory.wipe()
    }

    private fun insertTestData() {
        transaction {
            // Insert Locations
            val loc1Id = LocationsTable.insert {
                it[LocationsTable.id] = 1
                it[LocationsTable.name] = "Deertrack Path"
                it[LocationsTable.area] = Area.FIELDLANDS
            } get LocationsTable.id

            val loc2Id = LocationsTable.insert {
                it[LocationsTable.id] = 2
                it[LocationsTable.name] = "Golden Lowlands"
                it[LocationsTable.area] = Area.MIRELANDS
            } get LocationsTable.id

            // Insert Pokemons
            PokemonsTable.insert {
                it[PokemonsTable.id] = 25
                it[PokemonsTable.generalId] = 25
                it[PokemonsTable.name] = "Pikachu"
                it[PokemonsTable.types] = listOf(Type.ELECTRIC)
                it[PokemonsTable.locations] = listOf(loc1Id)
                it[PokemonsTable.toDos] = mapOf()
            }

            PokemonsTable.insert {
                it[PokemonsTable.id] = 74
                it[PokemonsTable.generalId] = 74
                it[PokemonsTable.name] = "Geodude"
                it[PokemonsTable.types] = listOf(Type.ROCK, Type.GROUND)
                it[PokemonsTable.locations] = listOf(loc1Id, loc2Id)
                it[PokemonsTable.toDos] = mapOf()
            }

            PokemonsTable.insert {
                it[PokemonsTable.id] = 95
                it[PokemonsTable.generalId] = 95
                it[PokemonsTable.name] = "Onix"
                it[PokemonsTable.types] = listOf(Type.ROCK, Type.GROUND)
                it[PokemonsTable.locations] = listOf(loc2Id)
                it[PokemonsTable.toDos] = mapOf()
            }
        }
    }

    @Test
    fun givenDefaultCriteria_whenReadAll_thenReturnsAllRows() = runBlocking {
        val result = dao.readAll(QueryCriteria())

        assertEquals(3, result.size)
    }

    @Test
    fun givenCriteriaWithArea_whenReadAll_thenReturnsCorrespondingRows() = runBlocking {
        val criteria = QueryCriteria(area = Area.MIRELANDS)
        val result = dao.readAll(criteria)
        
        assertEquals(2, result.size)
        assertTrue(result.any { it.name == "Geodude" })
        assertTrue(result.any { it.name == "Onix" })
        assertTrue(result.none { it.name == "Pikachu" })
    }

    @Test
    fun givenCriteriaWithName_whenReadAll_thenReturnsCorrespondingRow() = runBlocking {
        val criteria = QueryCriteria(name = "pika")
        val result = dao.readAll(criteria)
        
        assertEquals(1, result.size)
        assertEquals("Pikachu", result.first().name)
    }

    @Test
    fun givenCriteriaWithNumber_whenReadAll_thenReturnsCorrespondingRow() = runBlocking {
        val criteria = QueryCriteria(number = 95)
        val result = dao.readAll(criteria)

        assertEquals(1, result.size)
        assertEquals("Onix", result.first().name)
    }

    @Test
    fun givenExistingId_whenRead_thenReturnExistingRow() = runBlocking {
        val id = 25
        val result = dao.read(id)

        assertEquals("Pikachu", result.name)
        assertEquals(id, result.id)
    }
}
