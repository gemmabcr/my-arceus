package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonEntry(val pokemon_species: PokemonSpecies)
