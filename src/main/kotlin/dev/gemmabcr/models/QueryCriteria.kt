package dev.gemmabcr.models

import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.models.pokemons.todo.ToDo

data class QueryCriteria(
    val name: String? = null,
    val number: Int? = null,
    val area: Area? = null,
    val type: Type? = null,
    val toDo: ToDo? = null,
    val completion: CompletionFilter = CompletionFilter.ALL,
    val onlyTeam: Boolean = false,
    val pagination: Pagination = Pagination()
) {
    fun isFiltered(): Boolean =
        name.isNullOrBlank().not() || listOf(number, area, type, toDo).any { it != null } ||
                completion != CompletionFilter.ALL || onlyTeam
}
