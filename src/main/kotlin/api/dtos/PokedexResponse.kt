package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokedexResponse(val pokemon_entries: List<PokemonEntry>)
