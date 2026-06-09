package dev.gemmabcr.views

import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.models.Pagination
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.pokemons.Type
import dev.gemmabcr.models.pokemons.todo.ToDo
import io.ktor.http.Parameters
import kotlin.text.toIntOrNull

class QueryCriteriaBuilder {
    private lateinit var toDos: List<ToDo>
    private var name: String? = null
    private var number: Int? = null
    private var area: Area? = null
    private var type: Type? = null
    private var toDo: ToDo? = null
    private var completion: CompletionFilter = CompletionFilter.ALL
    private var onlyTeam: Boolean = false
    private var page: Int = 1

    fun with(toDos: List<ToDo>) = apply {
        this.toDos = toDos
    }

    fun with(queryParameters: Parameters) = apply {
        this.name = queryParameters[QueryCriteriaType.NAME.key()]
        this.number = queryParameters[QueryCriteriaType.NUMBER.key()]?.toIntOrNull()
        val location = queryParameters[QueryCriteriaType.AREA.key()]
        this.area = Area.entries.firstOrNull { it.name == location }
        this.type = type(queryParameters)
        this.toDo = toDo(queryParameters)
        this.completion = completion(queryParameters)
        this.onlyTeam = queryParameters.contains(QueryCriteriaType.TEAM.key())
        this.page = queryParameters[QueryCriteriaType.PAGE.key()]?.toInt() ?: 1
    }

    private fun toDo(queryParameters: Parameters): ToDo? = ToDoBuilder()
        .with(toDos)
        .with(queryParameters[QueryCriteriaType.TO_DO.key()]?.toInt())
        .build()

    private fun type(queryParameters: Parameters): Type? = queryParameters[QueryCriteriaType.TYPE.key()]?.let {
        Type.entries.firstOrNull { type -> type.name == it }
    }

    private fun completion(queryParameters: Parameters): CompletionFilter =
        queryParameters[QueryCriteriaType.COMPLETION.key()]?.let { value ->
            CompletionFilter.entries.firstOrNull { it.name == value }
        } ?: CompletionFilter.ALL

    fun build(): QueryCriteria = QueryCriteria(
        name = name,
        number = number,
        area = area,
        type = type,
        toDo = toDo,
        completion = completion,
        onlyTeam = onlyTeam,
        pagination = Pagination(page)
    )
}
