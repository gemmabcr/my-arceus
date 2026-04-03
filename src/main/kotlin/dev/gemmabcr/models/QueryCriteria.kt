package dev.gemmabcr.models

import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.todo.ToDo

data class QueryCriteria(
    val name: String? = null,
    val number: Int? = null,
    val area: Area? = null,
    val toDo: ToDo? = null,
    val pagination: Pagination = Pagination()
) {
    fun isFiltered(): Boolean = name.isNullOrBlank().not() || number != null || area != null
}
