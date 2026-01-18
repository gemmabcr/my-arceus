package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.database.tables.ToDosTable
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
            location = row[PokemonsTable.locations],
            toDos = toDtos(row[PokemonsTable.toDos])
        )
    }

    private suspend fun toDtos(ids: List<Int>): List<ToDoDto> = DatabaseFactory.dbQuery {
        ToDosTable.selectAll()
            .where(ToDosTable.id inList ids)
            .map { row -> ToDoDto(
                row[ToDosTable.id],
                row[ToDosTable.description],
            ) }
    }

    override suspend fun get(id: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq id)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }
}
