package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.AttackToDoType
import dev.gemmabcr.models.pokemons.todo.CaughtToDoType
import dev.gemmabcr.models.pokemons.todo.DefeatedToDoType
import dev.gemmabcr.models.pokemons.todo.SearchToDoType
import dev.gemmabcr.models.pokemons.todo.SpecificToDo
import dev.gemmabcr.models.pokemons.todo.ToDoType

class ToDoTypeAdapter(private val toDoType: ToDoType<*>) {
    fun text(): String = when (toDoType) {
        is AttackToDoType -> AttackToDoAdapter(toDoType).text()
        is CaughtToDoType -> CaughtToDoAdapter(toDoType).text()
        is DefeatedToDoType -> DefeatedToDoAdapter(toDoType).text()
        is SearchToDoType -> SearchToDoAdapter(toDoType).text()
        is SpecificToDo -> SpecificToDoAdapter(toDoType).text()
    }
}
