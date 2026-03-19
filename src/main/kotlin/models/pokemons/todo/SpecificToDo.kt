package dev.gemmabcr.models.pokemons.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("specific")
data class SpecificToDo(
    override val condition: SpecificToDoType
) : ToDo<SpecificToDoType>()
