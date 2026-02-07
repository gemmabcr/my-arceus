package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.LocationDto
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.tables.LocationsTable
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.QueryCriteria
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.selectAll

class ExposedDao : PokemonService {
    override suspend fun readAll(criteria: QueryCriteria): List<PokemonDto> = DatabaseFactory.dbQuery {
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
    ): Boolean = when (criteria.isFiltered()) {
        true -> matchNumber(row, criteria.number) || matchName(row, criteria.name) || matchArea(row, filterLocationIds)
        false -> true
    }

    private fun matchNumber(
        row: ResultRow,
        number: Int?,
    ): Boolean = number != null && row[PokemonsTable.id] == number

    private fun matchName(
        row: ResultRow,
        name: String?,
    ): Boolean = name != null && row[PokemonsTable.name].lowercase().contains(name.lowercase())

    private fun matchArea(
        row: ResultRow,
        filterLocationIds: List<Int>?,
    ): Boolean = filterLocationIds != null && row[PokemonsTable.locations].any { it in filterLocationIds }

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

    override suspend fun read(id: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq id)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }
}
