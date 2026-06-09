package database

import dev.gemmabcr.database.ExposedPokemonDao
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.models.Pagination
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.Type
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExposedPokemonDaoTest {
    private val dao = ExposedPokemonDao()

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
    fun givenDefaultCriteria_whenPokemonsRows() = runBlocking {
        val result = dao.pokemons(QueryCriteria(), null)

        assertEquals(3, result.results.size)
        assertEquals(false, result.hasNextPage)
        assertEquals(3, result.totalResults)
    }

    @Test
    fun givenCriteriaWithArea_whenPokemons_thenReturnsCorrespondingRows() = runBlocking {
        val criteria = QueryCriteria(area = Area.MIRELANDS)
        val result = dao.pokemons(criteria, null)
        
        assertEquals(2, result.results.size)
        assertTrue(result.results.any { it.name == "Geodude" })
        assertTrue(result.results.any { it.name == "Onix" })
        assertTrue(result.results.none { it.name == "Pikachu" })
    }

    @Test
    fun givenCriteriaWithName_whenPokemons_thenReturnsCorrespondingRow() = runBlocking {
        val criteria = QueryCriteria(name = "pika")
        val result = dao.pokemons(criteria, null)
        
        assertEquals(1, result.results.size)
        assertEquals("Pikachu", result.results.first().name)
    }

    @Test
    fun givenCriteriaWithType_whenPokemons_thenReturnsCorrespondingRows() = runBlocking {
        val criteria = QueryCriteria(type = Type.ROCK)
        val result = dao.pokemons(criteria, null)

        assertEquals(2, result.results.size)
        assertTrue(result.results.any { it.name == "Geodude" })
        assertTrue(result.results.any { it.name == "Onix" })
        assertTrue(result.results.none { it.name == "Pikachu" })
    }

    @Test
    fun givenCriteriaWithNumber_whenPokemons_thenReturnsCorrespondingRow() = runBlocking {
        val criteria = QueryCriteria(number = 95)
        val result = dao.pokemons(criteria, null)

        assertEquals(1, result.results.size)
        assertEquals("Onix", result.results.first().name)
    }

    @Test
    fun givenExistingId_whenPokemon_thenReturnExistingRow() = runBlocking {
        val id = 25
        val result = dao.pokemon(id)

        assertEquals("Pikachu", result.name)
        assertEquals(id, result.id)
    }

    @Test
    fun givenCriteriaWithPage_whenPokemons_thenReturnsRowsFromThatPage() = runBlocking {
        transaction {
            (101..105).forEach { i ->
                PokemonsTable.insert {
                    it[PokemonsTable.id] = i
                    it[PokemonsTable.generalId] = i
                    it[PokemonsTable.name] = "Pokemon $i"
                    it[PokemonsTable.types] = listOf(Type.NORMAL)
                    it[PokemonsTable.locations] = listOf()
                    it[PokemonsTable.toDos] = mapOf()
                }
            }
        }
        
        val pagination = Pagination(page = 1, pageSize = 3)
        val criteriaPage1 = QueryCriteria(pagination = pagination)
        val resultPage1 = dao.pokemons(criteriaPage1, null)
        
        assertEquals(3, resultPage1.results.size, "Expected 3 items in page 1, but got ${resultPage1.results.size}")
        assertTrue(resultPage1.hasNextPage)
        assertEquals(8, resultPage1.totalResults)
        assertEquals(25, resultPage1.results[0].id)
        assertEquals(74, resultPage1.results[1].id)
        assertEquals(95, resultPage1.results[2].id)

        val pagination2 = Pagination(page = 2, pageSize = 3)
        val criteriaPage2 = QueryCriteria(pagination = pagination2)
        val resultPage2 = dao.pokemons(criteriaPage2, null)
        assertEquals(3, resultPage2.results.size)
        assertTrue(resultPage2.hasNextPage)
        assertEquals(101, resultPage2.results[0].id)

        val pagination3 = Pagination(page = 3, pageSize = 3)
        val criteriaPage3 = QueryCriteria(pagination = pagination3)
        val resultPage3 = dao.pokemons(criteriaPage3, null)
        assertEquals(2, resultPage3.results.size)
        assertEquals(false, resultPage3.hasNextPage)
        assertEquals(104, resultPage3.results[0].id)
    }
}
