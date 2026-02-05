package models

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.QueryCriteria
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
}
