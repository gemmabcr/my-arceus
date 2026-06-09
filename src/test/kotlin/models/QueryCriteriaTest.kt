package models

import dev.gemmabcr.models.CompletionFilter
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.Type
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QueryCriteriaTest {
    @Test
    fun givenDefaultCriteria_whenIsFiltered_thenReturnsFalse() {
        val criteria = QueryCriteria()

        assertFalse(criteria.isFiltered())
    }

    @Test
    fun givenBlankString_whenIsFiltered_thenReturnsFalse() {
        val criteria = QueryCriteria(name = "")

        assertFalse(criteria.isFiltered())
    }

    @Test
    fun givenWithName_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(name = "leo")

        assertTrue(criteria.isFiltered())
    }

    @Test
    fun givenWithNumber_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(number = 12)

        assertTrue(criteria.isFiltered())
    }

    @Test
    fun givenWithArea_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(area = Area.entries.random())

        assertTrue(criteria.isFiltered())
    }

    @Test
    fun givenWithType_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(type = Type.entries.random())

        assertTrue(criteria.isFiltered())
    }

    @Test
    fun givenOnlyTeam_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(onlyTeam = true)

        assertTrue(criteria.isFiltered())
    }

    @Test
    fun givenCompletionFilter_whenIsFiltered_thenReturnsTrue() {
        val criteria = QueryCriteria(completion = CompletionFilter.COMPLETED)

        assertTrue(criteria.isFiltered())
    }
}
