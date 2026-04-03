package dev.gemmabcr.models.pokemons.todo

import dev.gemmabcr.models.pokemons.Attack
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("attack")
data class AttackToDoType(
    override val condition: Attack
) : ToDoType<Attack>()
