package dev.gemmabcr.views

import dev.gemmabcr.models.pokemons.todo.ToDo

class ToDoBuilder {
    private lateinit var toDos: List<ToDo>
    private var toDo: ToDo? = null

    fun with(toDos: List<ToDo>) = apply {
        this.toDos = toDos
    }

    fun with(id: Int?) = apply {
        if (id != null) {
            toDo = toDos.first { it.id == id }
        }
    }

    fun build(): ToDo? = toDo
}
