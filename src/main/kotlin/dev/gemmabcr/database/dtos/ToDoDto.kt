package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.pokemons.todo.ToDoType
import dev.gemmabcr.models.pokemons.todo.ToDoCondition
import kotlinx.serialization.Serializable

@Serializable
data class ToDoDto(
    val id: Int,
    val description: ToDoType<out ToDoCondition>,
    val goal: Int = 0,
)
