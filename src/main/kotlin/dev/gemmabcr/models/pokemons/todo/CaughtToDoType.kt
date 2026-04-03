package dev.gemmabcr.models.pokemons.todo

import dev.gemmabcr.models.pokemons.CaughtCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("caught")
data class CaughtToDoType(
    override val condition: CaughtCondition?
) : ToDoType<CaughtCondition>()
