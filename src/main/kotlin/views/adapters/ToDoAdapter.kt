package dev.gemmabcr.views.adapters

import dev.gemmabcr.models.pokemons.todo.AttackToDo
import dev.gemmabcr.models.pokemons.todo.CaughtToDo
import dev.gemmabcr.models.pokemons.todo.DefeatedToDo
import dev.gemmabcr.models.pokemons.todo.SearchToDo
import dev.gemmabcr.models.pokemons.todo.SpecificToDo
import dev.gemmabcr.models.pokemons.todo.ToDo

class ToDoAdapter(private val toDo: ToDo<*>) {
    fun text(): String = when (toDo) {
        is AttackToDo -> AttackToDoAdapter(toDo).text()
        is CaughtToDo -> CaughtToDoAdapter(toDo).text()
        is DefeatedToDo -> DefeatedToDoAdapter(toDo).text()
        is SearchToDo -> SearchToDoAdapter(toDo).text()
        is SpecificToDo -> SpecificToDoAdapter(toDo).text()
    }
}
