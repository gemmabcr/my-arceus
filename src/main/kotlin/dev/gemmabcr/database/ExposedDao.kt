package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.LocationDto
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
import dev.gemmabcr.database.tables.UserToDosTable
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.QueryCriteria
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

class ExposedDao : PokemonService {
    override suspend fun pokemons(criteria: QueryCriteria): List<PokemonDto> = DatabaseFactory.dbQuery {
        val filterLocationIds = when {
            criteria.area != null -> {
                LocationsTable.selectAll()
                    .where(LocationsTable.area eq criteria.area)
                    .map { it[LocationsTable.id] }
            }

            else -> null
        }

        PokemonsTable.selectAll()
            .orderBy(PokemonsTable.id)
            .filter { row -> filter(row, criteria, filterLocationIds) }
            .drop(criteria.pagination.offset.toInt())
            .take(criteria.pagination.pageSize)
            .map { pokemonDto(it) }
    }

    private fun filter(
        row: ResultRow,
        criteria: QueryCriteria,
        filterLocationIds: List<Int>?
    ): Boolean {
        if (criteria.isFiltered().not()) {
            return true
        }
        val matchNumber = criteria.number.let { it == null || row[PokemonsTable.id] == it }
        val matchName = criteria.name.let { it == null || row[PokemonsTable.name].lowercase().contains(it.lowercase()) }
        val matchArea = filterLocationIds == null || row[PokemonsTable.locations].any { it in filterLocationIds }
        return matchNumber && matchName && matchArea
    }

    private suspend fun pokemonDto(row: ResultRow): PokemonDto = PokemonDto(
        id = row[PokemonsTable.id],
        generalId = row[PokemonsTable.generalId],
        name = row[PokemonsTable.name],
        types = row[PokemonsTable.types],
        location = locations(row[PokemonsTable.locations]),
        toDos = toDtos(row[PokemonsTable.toDos]),
        specialCondition = row[PokemonsTable.specialCondition],
    )

    private suspend fun locations(ids: List<Int>): List<LocationDto> = DatabaseFactory.dbQuery {
        if (ids.isEmpty()) return@dbQuery emptyList()
        LocationsTable.selectAll()
            .where(LocationsTable.id inList ids)
            .map { row ->
                LocationDto(
                    row[LocationsTable.id],
                    row[LocationsTable.name],
                    row[LocationsTable.area],
                )
            }
    }

    private suspend fun toDtos(toDosMap: Map<String, Int>): List<ToDoDto> = DatabaseFactory.dbQuery {
        val ids = toDosMap.keys.map { it.toInt() }
        if (ids.isEmpty()) return@dbQuery emptyList()
        ToDosTable.selectAll()
            .where(ToDosTable.id inList ids)
            .map { row ->
                val id = row[ToDosTable.id]
                ToDoDto(
                    id = id,
                    description = row[ToDosTable.description],
                    goal = toDosMap[id.toString()] ?: 0
                )
            }
    }

    override suspend fun pokemon(pokemon: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq pokemon)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }

    override suspend fun userTodos(user: Int): List<UserToDoDto> = DatabaseFactory.dbQuery {
        UserToDosTable.selectAll()
            .where(UserToDosTable.userId eq user)
            .map(::userToDoDto)
    }

    private fun userToDoDto(row: ResultRow): UserToDoDto = UserToDoDto(
        userId = row[UserToDosTable.userId],
        pokemonId = row[UserToDosTable.pokemonId],
        todoId = row[UserToDosTable.todoId],
        done = row[UserToDosTable.done],
    )

    override suspend fun userTodos(user: Int, pokemonId: Int): List<UserToDoDto> = DatabaseFactory.dbQuery {
        UserToDosTable.selectAll()
            .where(UserToDosTable.userId eq user and (UserToDosTable.pokemonId eq pokemonId))
            .map(::userToDoDto)
    }
}
