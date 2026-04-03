package dev.gemmabcr.models.pokemons.todo

data class ProgressToDo(
    val id: Int,
    val toDoType: ToDoType<*>,
    val done: Int,
    val goal: Int
) {
    fun completed(): Boolean = done >= goal
}
