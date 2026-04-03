package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserTeamDto(
    val userId: Int,
    val pokemonId: Int,
)
