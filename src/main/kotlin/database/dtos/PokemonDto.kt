package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.models.Type
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val location: List<Int>,
    val toDos: List<ToDoDto>
)
