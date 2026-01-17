package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokedexResponse(
    val name: String,
    val pokemon_entries: List<PokemonEntryDto>,
    val region: NameUrlDto,
)
