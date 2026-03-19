package dev.gemmabcr.models.pokemons

import dev.gemmabcr.models.pokemons.todo.ToDo

interface BasePokemon {
    val hisuiId: Int
    val generalId: Int
    val name: String
    val types: List<Type>
    val toDos: List<ToDo<*>>
}
