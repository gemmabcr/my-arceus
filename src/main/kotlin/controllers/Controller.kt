package dev.gemmabcr.controllers

import dev.gemmabcr.api.PokemonApi
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.ToDo

class Controller(private val api: PokemonApi, private val dao: PokemonService) {

    suspend fun pokemons(criteria: QueryCriteria): List<Pokemon> {
        val result: List<PokemonDto> = dao.readAll(criteria)
        return result.map(::createPokemon)
    }

    private fun createPokemon(dto: PokemonDto): Pokemon = Pokemon(
        dto.id,
        dto.generalId,
        dto.name,
        dto.types,
        dto.location.map { Location(it.id, it.name, it.area) },
        dto.toDos.map { toDo -> ToDo(toDo.description, toDo.goal) },
        dto.specialCondition
    )

    suspend fun pokemon(id: Int): Pokemon {
        val result: PokemonDto = dao.read(id)
        return createPokemon(result)
    }
}
