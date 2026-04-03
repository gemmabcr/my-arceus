package dev.gemmabcr.models.pokemons.todo

import dev.gemmabcr.models.pokemons.Type
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("defeated")
data class DefeatedToDoType(
    override val condition: Type?
) : ToDoType<Type>()
