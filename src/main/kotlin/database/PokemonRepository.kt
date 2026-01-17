package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.tables.PokemonsTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class PokemonRepository {
    suspend fun list(): List<PokemonDto> = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .orderBy(PokemonsTable.id, SortOrder.DESC)
            .map { pokemonDto(it) }
    }

    private fun pokemonDto(row: ResultRow): PokemonDto = PokemonDto(
        id = row[PokemonsTable.id],
        location = row[PokemonsTable.locations],
        toDos = row[PokemonsTable.toDos]
    )

    suspend fun get(id: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.select(PokemonsTable.id eq id)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }
}
