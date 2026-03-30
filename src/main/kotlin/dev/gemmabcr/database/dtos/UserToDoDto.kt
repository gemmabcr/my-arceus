package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserToDoDto(
    val userId: Int,
    val pokemonId: Int,
    val todoId: Int,
    val done: Int,
)
