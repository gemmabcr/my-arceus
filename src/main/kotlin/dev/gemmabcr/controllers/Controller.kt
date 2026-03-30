package dev.gemmabcr.controllers

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.pokemons.DetailedPokemon
import dev.gemmabcr.models.pokemons.Location
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.models.pokemons.todo.ProgressToDo

class Controller(private val dao: PokemonService) {

    suspend fun pokemons(criteria: QueryCriteria, session: Session): List<Pokemon> {
        val pokemons = dao.pokemons(criteria).map(::createPokemon)
        val userId = session.user ?: return pokemons
        val userTodosMap: Map<Int, List<UserToDoDto>> = dao.userTodos(userId).groupBy { it.pokemonId }

        return pokemons.map { pokemon ->
            val maybeTodos = userTodosMap[pokemon.hisuiId] ?: return@map pokemon
            pokemon.copy(toDos = progressToDos(pokemon.toDos, maybeTodos))
        }
    }

    private fun createPokemon(dto: PokemonDto): Pokemon =
        Pokemon(
            dto.id,
            dto.generalId,
            dto.name,
            dto.types,
            toDos(dto.toDos)
        )

    fun toDos(toDos: List<ToDoDto>): List<ProgressToDo> = toDos.map {
        ProgressToDo(
            it.id,
            it.description,
            0,
            it.goal
        )
    }

    suspend fun pokemon(id: Int, session: Session): DetailedPokemon {
        val result: PokemonDto = dao.pokemon(id)
        val pokemon = createDetailedPokemon(result)
        val userId = session.user ?: return pokemon

        val userTodos: List<UserToDoDto> = dao.userTodos(userId, id)
        return pokemon.copy(toDos = progressToDos(pokemon.toDos, userTodos))
    }

    private fun progressToDos(
        toDos: List<ProgressToDo>,
        userTodos: List<UserToDoDto>
    ): List<ProgressToDo> = toDos.map { toDo ->
        val progressToDo = userTodos.firstOrNull { it.todoId == toDo.id }
        when (progressToDo) {
            null -> toDo
            else -> toDo.copy(done = progressToDo.done)
        }
    }

    private fun createDetailedPokemon(dto: PokemonDto): DetailedPokemon =
        DetailedPokemon(
            dto.id,
            dto.generalId,
            dto.name,
            dto.types,
            dto.location.map { Location(it.id, it.name, it.area) },
            toDos(dto.toDos),
            dto.specialCondition,
            emptyList()
        )
}
