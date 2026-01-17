package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val types: List<PokemonTypeSlots>,
)
