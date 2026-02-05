package models

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.QueryCriteria
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PokemonTest {
    @Test
    fun givenSomeSubstringOfItsName_whenMatch_thenReturnsTrue() {
        val pokemon = PokemonBuilder().build()
        val search = "pika"
        val criteria = QueryCriteria(name = search)

        assertTrue(pokemon.match(criteria))
    }

    @Test
    fun givenNotSubstringOfItsName_whenMatch_thenReturnsFalse() {
        val pokemon = PokemonBuilder().build()
        val search = "leo"
        val criteria = QueryCriteria(name = search)

        assertFalse(pokemon.match(criteria))
    }

    @Test
    fun givenItsNumber_whenMatch_thenReturnsTrue() {
        val number = 234
        val pokemon = PokemonBuilder().with(number).build()
        val criteria = QueryCriteria(number = number)

        assertTrue(pokemon.match(criteria))
    }

    @Test
    fun givenNotItsNumber_whenMatch_thenReturnsFalse() {
        val number = 34
        val pokemon = PokemonBuilder().build()
        val criteria = QueryCriteria(number = number)

        assertFalse(pokemon.match(criteria))
    }

    @Test
    fun givenItsArea_whenMatch_thenReturnsTrue() {
        val area = Area.entries.random()
        val pokemon = PokemonBuilder().with(area).build()
        val criteria = QueryCriteria(area = area)

        assertTrue(pokemon.match(criteria))
    }

    @Test
    fun givenNotItsArea_whenMatch_thenReturnsTrue() {
        val area = Area.DISTORTION
        val pokemon = PokemonBuilder().build()
        val criteria = QueryCriteria(area = area)

        assertFalse(pokemon.match(criteria))
    }
}
