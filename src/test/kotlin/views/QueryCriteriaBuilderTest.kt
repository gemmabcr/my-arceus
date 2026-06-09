package views

import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.views.QueryCriteriaBuilder
import io.ktor.http.parametersOf
import kotlin.test.Test
import kotlin.test.assertEquals

class QueryCriteriaBuilderTest {
    @Test
    fun givenCompletionParameter_whenBuildingCriteria_thenUsesSelectedFilter() {
        val criteria = QueryCriteriaBuilder()
            .with(emptyList())
            .with(parametersOf("completion", CompletionFilter.COMPLETED.name))
            .build()

        assertEquals(CompletionFilter.COMPLETED, criteria.completion)
    }

    @Test
    fun givenUnknownCompletionParameter_whenBuildingCriteria_thenUsesAll() {
        val criteria = QueryCriteriaBuilder()
            .with(emptyList())
            .with(parametersOf("completion", "UNKNOWN"))
            .build()

        assertEquals(CompletionFilter.ALL, criteria.completion)
    }
}
