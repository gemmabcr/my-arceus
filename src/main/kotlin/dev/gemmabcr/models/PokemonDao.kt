package dev.gemmabcr.models

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.models.pokemons.todo.ToDo

interface PokemonDao {
    suspend fun pokemons(criteria: QueryCriteria, userId: Int? = null): QueryResult<PokemonDto>

    suspend fun pokemon(pokemon: Int): PokemonDto

    suspend fun todos(): List<ToDo>
}
