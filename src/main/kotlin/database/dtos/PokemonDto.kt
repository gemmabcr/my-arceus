package dev.gemmabcr.database.dtos

import dev.gemmabcr.database.dtos.database.dtos.LocationDto
import dev.gemmabcr.models.Type
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val location: List<LocationDto>,
    val toDos: List<ToDoDto>
)
