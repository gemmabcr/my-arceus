package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.Type
import dev.gemmabcr.models.SpecialCondition
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val generalId: Int,
    val name: String,
    val types: List<Type>,
    val location: List<LocationDto>,
    val toDos: List<ToDoDto>,
    val specialCondition: SpecialCondition?,
)
