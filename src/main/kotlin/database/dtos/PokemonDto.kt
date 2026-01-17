package dev.gemmabcr.database.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val location: List<Int>,
    val toDos: List<Int>
)
