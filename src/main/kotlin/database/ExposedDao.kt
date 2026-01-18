package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.database.dtos.LocationDto
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
import dev.gemmabcr.database.tables.database.tables.LocationsTable
import dev.gemmabcr.models.PokemonService
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.selectAll

class ExposedDao : PokemonService {
    override suspend fun list(): List<PokemonDto> = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .orderBy(PokemonsTable.id)
            .map { pokemonDto(it) }
    }

    private suspend fun pokemonDto(row: ResultRow): PokemonDto {
        val id = row[PokemonsTable.id]
        return PokemonDto(
            id = id,
            name = row[PokemonsTable.name],
            types = row[PokemonsTable.types],
            location = locations(row[PokemonsTable.locations]),
            toDos = toDtos(row[PokemonsTable.toDos])
        )
    }

    private suspend fun locations(ids: List<Int>): List<LocationDto> = DatabaseFactory.dbQuery {
        LocationsTable.selectAll()
            .where(LocationsTable.id inList ids)
            .map { row -> LocationDto(
                row[LocationsTable.id],
                row[LocationsTable.name],
                row[LocationsTable.area],
            ) }
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

    override suspend fun get(id: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq id)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }
}
