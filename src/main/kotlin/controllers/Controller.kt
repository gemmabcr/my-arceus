package dev.gemmabcr.controllers

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.models.DetailedPokemon
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.ToDo

class Controller(private val dao: PokemonService) {

    suspend fun pokemons(criteria: QueryCriteria): List<Pokemon> {
        val result: List<PokemonDto> = dao.readAll(criteria)
        return result.map(::createPokemon)
    }

    private fun createPokemon(dto: PokemonDto): Pokemon = Pokemon(
        dto.id,
        dto.generalId,
        dto.name,
        dto.types,
        dto.toDos.map { toDo -> ToDo(toDo.description, toDo.goal) },
    )

    suspend fun pokemon(id: Int): DetailedPokemon {
        val result: PokemonDto = dao.read(id)
        return createDetailedPokemon(result)
    }

    private fun createDetailedPokemon(dto: PokemonDto): DetailedPokemon = DetailedPokemon(
        dto.id,
        dto.generalId,
        dto.name,
        dto.types,
        dto.location.map { Location(it.id, it.name, it.area) },
        dto.toDos.map { toDo -> ToDo(toDo.description, toDo.goal) },
        dto.specialCondition,
        emptyList()
    )
}
