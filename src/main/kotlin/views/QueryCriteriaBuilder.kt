package dev.gemmabcr.views

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Pagination
import dev.gemmabcr.models.QueryCriteria
import io.ktor.http.Parameters
import kotlin.text.toIntOrNull

class QueryCriteriaBuilder {
    private var name: String? = null
    private var number: Int? = null
    private var area: Area? = null
    private var page: Int = 1

    fun with(queryParameters: Parameters) = apply {
        this.name = queryParameters[QueryCriteriaType.NAME.key()]
        this.number = queryParameters[QueryCriteriaType.NUMBER.key()]?.toIntOrNull()
        val location = queryParameters[QueryCriteriaType.AREA.key()]
        this.area = Area.entries.firstOrNull { it.name == location }
        this.page = queryParameters[QueryCriteriaType.PAGE.key()]?.toInt() ?: 1
    }

    fun build(): QueryCriteria = QueryCriteria(name, number, area, Pagination(page))
}
