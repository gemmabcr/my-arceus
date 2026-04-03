package dev.gemmabcr.models.pokemons.todo

data class ToDo(
    val id: Int,
    val description: ToDoType<out ToDoCondition>,
)
