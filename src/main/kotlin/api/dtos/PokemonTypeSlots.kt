package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeSlots(val slot: Int, val type: NameUrlDto)
