package dev.gemmabcr.database.dtos

import dev.gemmabcr.models.pokemons.todo.ToDo
import dev.gemmabcr.models.pokemons.todo.ToDoCondition
import kotlinx.serialization.Serializable

@Serializable
data class ToDoDto(
    val id: Int,
    val description: ToDo<out ToDoCondition>,
    val goal: Int = 0,
)
