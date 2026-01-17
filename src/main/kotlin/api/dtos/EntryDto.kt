package dev.gemmabcr.api.dtos

import kotlinx.serialization.Serializable

@Serializable
data class EntryDto(
    val evolution_chain: UrlDto?,
    val evolves_from_species: NameUrlDto?,
    val id: Int,
)
