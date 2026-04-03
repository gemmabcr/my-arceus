package dev.gemmabcr.models

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.models.pokemons.todo.ToDo

interface PokemonService {
    suspend fun pokemons(criteria: QueryCriteria): List<PokemonDto>

    suspend fun pokemon(pokemon: Int): PokemonDto

    suspend fun todos(): List<ToDo>

    suspend fun userTodos(user: Int): List<UserToDoDto>

    suspend fun userTodos(user: Int, pokemonId: Int): List<UserToDoDto>
}
