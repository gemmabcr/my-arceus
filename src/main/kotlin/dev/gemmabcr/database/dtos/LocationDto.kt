package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.pokemons.Area
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val area: Area,
)
