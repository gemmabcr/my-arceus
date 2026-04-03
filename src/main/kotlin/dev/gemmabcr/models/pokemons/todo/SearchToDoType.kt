package dev.gemmabcr.models.pokemons.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("search")
data class SearchToDoType(
    override val condition: SearchTask
) : ToDoType<SearchTask>()
