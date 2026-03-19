package dev.gemmabcr.models.pokemons.todo

import kotlinx.serialization.Serializable

@Serializable
sealed class ToDo<TYPE: ToDoCondition> {
    abstract val condition: TYPE?
}

