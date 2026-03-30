package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.pokemons.CaughtCondition
import dev.gemmabcr.models.pokemons.Type
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDto(
    val id: Int,
    val generalId: Int,
    val name: String,
    val types: List<Type>,
    val location: List<LocationDto>,
    val toDos: List<ToDoDto>,
    val specialCondition: CaughtCondition?,
)
