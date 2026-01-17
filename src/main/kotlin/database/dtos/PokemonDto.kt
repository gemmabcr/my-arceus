package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val location: List<Int>,
    val toDos: List<Int>
)
