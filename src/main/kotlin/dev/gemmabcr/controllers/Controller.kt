package dev.gemmabcr.controllers

import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.UserToDoDto
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
        if (userId == null) {
            val result = pokemonDao.pokemons(criteria)
            return QueryResult(result.results.map(::createPokemon), result.hasNextPage)
        }

        val result = when (criteria.onlyUncompleted) {
            true -> allPokemons(criteria, userId)
            false -> pokemonDao.pokemons(criteria, userId)
        }
        val teamIds = userDao.team(userId).map { it.pokemonId }.toSet()
        val userTodosMap: Map<Int, List<UserToDoDto>> = userDao.todos(userId).groupBy { it.pokemonId }

        val pokemonsWithUserData = result.results.map(::createPokemon).map { pokemon ->
            val maybeTodos = userTodosMap[pokemon.hisuiId] ?: emptyList()
            pokemon.copy(
                toDos = progressToDos(pokemon.toDos, maybeTodos),
                inTeam = teamIds.contains(pokemon.hisuiId)
            )
        }
        val filteredPokemons = pokemonsWithUserData
            .let { pokemons ->
                if (criteria.onlyUncompleted) pokemons.filter { pokemon -> pokemon.isUncompleted() } else pokemons
            }

        return when (criteria.onlyUncompleted) {
            true -> {
                val fromIndex = criteria.pagination.offset.toInt().coerceAtMost(filteredPokemons.size)
                val toIndex = (fromIndex + criteria.pagination.pageSize).coerceAtMost(filteredPokemons.size)
                QueryResult(
                    results = filteredPokemons.subList(fromIndex, toIndex),
                    hasNextPage = toIndex < filteredPokemons.size
                )
            }

            false -> QueryResult(filteredPokemons, result.hasNextPage)
        }
    }

    private suspend fun allPokemons(criteria: QueryCriteria, userId: Int): QueryResult<PokemonDto> {
        val results = mutableListOf<PokemonDto>()
        var page = 1

        while (true) {
            val currentResult = pokemonDao.pokemons(
                criteria.copy(pagination = Pagination(page, criteria.pagination.pageSize)),
                userId
            )
            results += currentResult.results
            if (!currentResult.hasNextPage) {
                return QueryResult(results, false)
            }
            page++
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

    private fun toDos(toDos: List<ToDoDto>): List<ProgressToDo> = toDos.map {
        ProgressToDo(it.id, it.description, 0, it.goal)
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
                toDos(it.toDos),
                it.specialCondition,
                emptyList()
            )
        }
        val userId = session.user ?: return pokemon

        val userTodos: List<UserToDoDto> = userDao.todos(userId, id)
        val isInTeam = userDao.team(userId).any { it.pokemonId == id }
        return pokemon.copy(toDos = progressToDos(pokemon.toDos, userTodos), inTeam = isInTeam)
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
}

private const val MAX_TEAM_SIZE = 6
