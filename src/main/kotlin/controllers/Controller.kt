package dev.gemmabcr.controllers

import dev.gemmabcr.api.PokemonApi
import dev.gemmabcr.api.PokemonEntry
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.ToDo

class Controller(private val api: PokemonApi, private val dao: PokemonService) {
    private lateinit var pokemonList: List<Pokemon>

    suspend fun pokemons(): List<Pokemon> {
        maybeLoadPokemons()
        return pokemonList
    }

    private suspend fun maybeLoadPokemons() {
        if (::pokemonList.isInitialized.not()) {
            val todos: List<PokemonDto> = dao.list()
            val entries: List<PokemonEntry> = api.pokemons()

            pokemonList = todos.map { dto ->
                val entry = entries.first { it.hisuiId == dto.id }
                Pokemon(
                    dto.id,
                    entry.id,
                    dto.name,
                    dto.types,
                    dto.location.map { Location(it.id, it.name, it.area) },
                    dto.toDos.map { toDo -> ToDo(toDo.description, toDo.goal) },
                    dto.specialCondition
                )
            }
        }
    }

    suspend fun pokemon(id: Int): Pokemon {
        maybeLoadPokemons()
        return pokemonList.first { it.hisuiId == id }
    }
}
