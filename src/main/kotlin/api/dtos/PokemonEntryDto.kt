package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonEntryDto(
    val entry_number: Int,
    val pokemon_species: NameUrlDto,
)
