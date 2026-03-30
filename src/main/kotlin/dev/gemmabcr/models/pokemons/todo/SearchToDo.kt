package dev.gemmabcr.models.pokemons.todo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("search")
data class SearchToDo(
    override val condition: SearchTask
) : ToDo<SearchTask>()
