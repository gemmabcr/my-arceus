package database

import dev.gemmabcr.database.ExposedUserDao
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
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

class ExposedUserDaoTest {
    private val dao = ExposedUserDao()

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
    fun givenUserTeam_whenAddingAndRemovingPokemon_thenPersistsSelection() = runBlocking {
        dao.addPokemonToTeam(1, 25)
        dao.addPokemonToTeam(1, 74)

        val team = dao.team(1)

        assertEquals(listOf(25, 74), team.map { it.pokemonId })

        dao.removePokemonFromTeam(1, 25)

        assertEquals(listOf(74), dao.team(1).map { it.pokemonId })
    }

    @Test
    fun givenOnlyTeamCriteria_whenPokemons_thenReturnsOnlyUserTeamRows() = runBlocking {
        dao.addPokemonToTeam(1, 74)
        dao.addPokemonToTeam(1, 95)

        val result = dao.team(1)

        assertEquals(2, result.size)
        assertTrue(result.any { it.pokemonId == 74 })
        assertTrue(result.any { it.pokemonId == 95 })
        assertTrue(result.none { it.pokemonId == 25 })
    }
}
