package dev.gemmabcr.models.pokemons.todo

import dev.gemmabcr.models.pokemons.Attack
import dev.gemmabcr.models.pokemons.CaughtCondition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("attack")
data class AttackToDo(
    override val condition: Attack
) : ToDo<Attack>()
