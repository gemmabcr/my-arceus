package dev.gemmabcr.models.pokemons.todo

import kotlinx.serialization.Serializable

@Serializable
sealed class ToDoType<TYPE: ToDoCondition> {
    abstract val condition: TYPE?
}

