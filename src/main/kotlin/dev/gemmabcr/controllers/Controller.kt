package dev.gemmabcr.controllers

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.Pagination
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.QueryResult
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserDao
import dev.gemmabcr.models.pokemons.DetailedPokemon
import dev.gemmabcr.models.pokemons.Location
import dev.gemmabcr.models.pokemons.Pokemon
import dev.gemmabcr.models.pokemons.todo.ProgressToDo
import dev.gemmabcr.models.pokemons.todo.ToDo

class Controller(
    private val pokemonDao: PokemonDao,
    private val userDao: UserDao
) {
    suspend fun pokemons(criteria: QueryCriteria, session: Session): QueryResult<Pokemon> {
        val userId = session.user
        if (userId == null && criteria.completion == CompletionFilter.ALL) {
            val result = pokemonDao.pokemons(criteria)
            return QueryResult(result.results.map(::createPokemon), result.hasNextPage, result.totalResults)
        }

        val result = pokemonResult(criteria, userId)
        val pokemonsWithUserData = withUserData(result.results, userId)
        val filteredPokemons = pokemonsWithUserData.filterByCompletion(criteria.completion)

        return queryResult(criteria, result, filteredPokemons)
    }

    private suspend fun pokemonResult(
        criteria: QueryCriteria,
        userId: Int?,
    ): QueryResult<PokemonDto> =
        when (criteria.completion) {
            CompletionFilter.ALL -> pokemonDao.pokemons(criteria, userId)
            else -> allPokemons(criteria, userId)
        }

    private suspend fun withUserData(
        results: List<PokemonDto>,
        userId: Int?,
    ): List<Pokemon> {
        val teamIds = userId?.let { userDao.team(it).map { team -> team.pokemonId }.toSet() } ?: emptySet()
        val userTodosMap: Map<Int, List<UserToDoDto>> =
            userId?.let { userDao.todos(it).groupBy { todo -> todo.pokemonId } } ?: emptyMap()

        return results.map(::createPokemon).map { pokemon ->
            val maybeTodos = userTodosMap[pokemon.hisuiId] ?: emptyList()
            pokemon.copy(
                toDos = mergeProgress(pokemon.toDos, maybeTodos),
                inTeam = teamIds.contains(pokemon.hisuiId)
            )
        }
    }

    private suspend fun allPokemons(criteria: QueryCriteria, userId: Int?): QueryResult<PokemonDto> {
        val results = mutableListOf<PokemonDto>()
        var page = 1

        while (true) {
            val currentResult = pokemonDao.pokemons(
                criteria.copy(pagination = Pagination(page, criteria.pagination.pageSize)),
                userId
            )
            results += currentResult.results
            if (!currentResult.hasNextPage) {
                return QueryResult(results, false, results.size)
            }
            page++
        }
    }

    suspend fun toDos(): List<ToDo> = pokemonDao.todos()

    suspend fun pokemon(id: Int, session: Session): DetailedPokemon {
        val result: PokemonDto = pokemonDao.pokemon(id)
        val pokemon = result.let {
            DetailedPokemon(
                it.id,
                it.generalId,
                it.name,
                it.types,
                it.location.map { location -> Location(location.id, location.name, location.area) },
                progressToDos(it.toDos),
                it.specialCondition,
                emptyList()
            )
        }
        val userId = session.user ?: return pokemon

        val userTodos: List<UserToDoDto> = userDao.todos(userId, id)
        val isInTeam = userDao.team(userId).any { it.pokemonId == id }
        return pokemon.copy(toDos = mergeProgress(pokemon.toDos, userTodos), inTeam = isInTeam)
    }

    suspend fun team(session: Session): List<Pokemon> {
        val userId = session.user ?: return emptyList()
        return userDao.team(userId).map { teamPokemon ->
            createPokemon(pokemonDao.pokemon(teamPokemon.pokemonId)).copy(inTeam = true)
        }
    }

    suspend fun addPokemonToTeam(pokemonId: Int, session: Session): Boolean {
        val userId = session.user ?: return false
        val team = userDao.team(userId)
        return when {
            team.any { it.pokemonId == pokemonId } -> true
            team.size >= MAX_TEAM_SIZE -> false
            else -> {
                userDao.addPokemonToTeam(userId, pokemonId)
                true
            }
        }
    }

    suspend fun removePokemonFromTeam(pokemonId: Int, session: Session) {
        val userId = session.user ?: return
        userDao.removePokemonFromTeam(userId, pokemonId)
    }

}

private fun createPokemon(dto: PokemonDto): Pokemon =
    Pokemon(
        dto.id,
        dto.generalId,
        dto.name,
        dto.types,
        progressToDos(dto.toDos)
    )

private fun progressToDos(toDos: List<ToDoDto>): List<ProgressToDo> = toDos.map {
    ProgressToDo(it.id, it.description, 0, it.goal)
}

private fun List<Pokemon>.filterByCompletion(completionFilter: CompletionFilter): List<Pokemon> =
    when (completionFilter) {
        CompletionFilter.ALL -> this
        CompletionFilter.UNCOMPLETED -> filter { it.isUncompleted() }
        CompletionFilter.COMPLETED -> filter { it.isUncompleted().not() }
    }

private fun queryResult(
    criteria: QueryCriteria,
    result: QueryResult<PokemonDto>,
    filteredPokemons: List<Pokemon>,
): QueryResult<Pokemon> {
    if (criteria.completion == CompletionFilter.ALL) {
        return QueryResult(filteredPokemons, result.hasNextPage, result.totalResults)
    }

    val fromIndex = criteria.pagination.offset.toInt().coerceAtMost(filteredPokemons.size)
    val toIndex = (fromIndex + criteria.pagination.pageSize).coerceAtMost(filteredPokemons.size)
    return QueryResult(
        results = filteredPokemons.subList(fromIndex, toIndex),
        hasNextPage = toIndex < filteredPokemons.size,
        totalResults = filteredPokemons.size
    )
}

private fun mergeProgress(
    toDos: List<ProgressToDo>,
    userTodos: List<UserToDoDto>
): List<ProgressToDo> = toDos.map { toDo ->
    val progressToDo = userTodos.firstOrNull { it.todoId == toDo.id }
    when (progressToDo) {
        null -> toDo
        else -> toDo.copy(done = progressToDo.done)
    }
}

private const val MAX_TEAM_SIZE = 6
