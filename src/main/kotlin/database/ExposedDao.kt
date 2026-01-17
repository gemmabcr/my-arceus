package dev.gemmabcr.database

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.tables.PokemonsTable
import dev.gemmabcr.models.PokemonService
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll

class ExposedDao : PokemonService {
    override suspend fun list(): List<PokemonDto> = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .orderBy(PokemonsTable.id)
            .map { pokemonDto(it) }
    }

    private fun pokemonDto(row: ResultRow): PokemonDto {
        val id = row[PokemonsTable.id]
        return PokemonDto(
            id = id,
            location = row[PokemonsTable.locations],
            toDos = row[PokemonsTable.toDos]
        )
    }

    override suspend fun get(id: Int): PokemonDto = DatabaseFactory.dbQuery {
        PokemonsTable.selectAll()
            .where(PokemonsTable.id eq id)
            .limit(1)
            .map { pokemonDto(it) }
            .single()
    }
}
