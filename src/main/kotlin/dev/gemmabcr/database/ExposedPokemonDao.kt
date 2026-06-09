package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.LocationDto
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
import dev.gemmabcr.database.tables.UserTeamsTable
import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.QueryResult
import dev.gemmabcr.models.pokemons.todo.ToDo
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.selectAll

class ExposedPokemonDao : PokemonDao {
    private lateinit var toDos: List<ToDoDto>

    override suspend fun pokemons(
        criteria: QueryCriteria,
        userId: Int?
    ): QueryResult<PokemonDto> = DatabaseFactory.dbQuery {
        val filterLocationIds = criteria.area?.let { area ->
            LocationsTable.selectAll()
                .where(LocationsTable.area eq area)
                .map { it[LocationsTable.id] }
        }
        val teamIds = when {
            criteria.onlyTeam.not() -> null
            userId == null -> emptyList()
            else -> UserTeamsTable.selectAll()
                .where(UserTeamsTable.userId eq userId)
                .map { it[UserTeamsTable.pokemonId] }
        }

        val filteredPokemonRows = PokemonsTable.selectAll()
            .orderBy(PokemonsTable.id)
            .filter { row -> QueryCriteriaPokemonsTable.filter(row, criteria, filterLocationIds, teamIds) }

        val pokemonRows = filteredPokemonRows
            .drop(criteria.pagination.offset.toInt())
            .take(criteria.pagination.pageSize + 1)

        QueryResult(
            results = pokemonRows
                .take(criteria.pagination.pageSize)
                .map { pokemonDto(it) },
            hasNextPage = pokemonRows.size > criteria.pagination.pageSize,
            totalResults = filteredPokemonRows.size
        )
    }

    private fun pokemonDto(row: ResultRow): PokemonDto {
        val toDosMap = row[PokemonsTable.toDos]
        val ids = toDosMap.keys.map { it.toInt() }
        val toDosList = when {
            ids.isEmpty() -> emptyList()
            else -> {
                ToDosTable.selectAll()
                    .where(ToDosTable.id inList ids)
                    .map { toDoRow ->
                        val id = toDoRow[ToDosTable.id]
                        ToDoDto(id, toDoRow[ToDosTable.description], toDosMap[id.toString()] ?: 0)
                    }
            }
        }
        val locationIds = row[PokemonsTable.locations]
        val locations = when {
            locationIds.isEmpty() -> emptyList()
            else -> LocationsTable.selectAll()
                .where(LocationsTable.id inList ids)
                .map { locationRow ->
                    LocationDto(
                        locationRow[LocationsTable.id],
                        locationRow[LocationsTable.name],
                        locationRow[LocationsTable.area],
                    )
                }
        }

        return PokemonDto(
            id = row[PokemonsTable.id],
            generalId = row[PokemonsTable.generalId],
            name = row[PokemonsTable.name],
            types = row[PokemonsTable.types],
            location = locations,
            toDos = toDosList,
            specialCondition = row[PokemonsTable.specialCondition],
        )
    }

    override suspend fun pokemon(pokemon: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq pokemon)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }

    override suspend fun todos(): List<ToDo> = DatabaseFactory.dbQuery {
        if (::toDos.isInitialized.not()) {
            toDos = ToDosTable.selectAll().map { row ->
                ToDoDto(
                    id = row[ToDosTable.id],
                    description = row[ToDosTable.description],
                )
            }
        }
        return@dbQuery toDos.map { ToDo(it.id, it.description) }
    }
}
