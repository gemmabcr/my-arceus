package dev.gemmabcr.models.pokemons.todo

import dev.gemmabcr.models.pokemons.Type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("defeated")
data class DefeatedToDo(
    override val condition: Type?
) : ToDo<Type>()
