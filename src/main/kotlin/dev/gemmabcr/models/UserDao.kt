package dev.gemmabcr.models

import dev.gemmabcr.database.dtos.UserToDoDto
import dev.gemmabcr.database.dtos.UserTeamDto

interface UserDao {
    suspend fun todos(user: Int): List<UserToDoDto>

    suspend fun todos(user: Int, pokemonId: Int): List<UserToDoDto>

    suspend fun team(user: Int): List<UserTeamDto>

    suspend fun addPokemonToTeam(user: Int, pokemonId: Int)

    suspend fun removePokemonFromTeam(user: Int, pokemonId: Int)
}
