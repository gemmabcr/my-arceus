package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonSpecies(
    val name: String,
    val url: String
)
