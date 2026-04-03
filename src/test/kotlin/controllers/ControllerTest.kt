package controllers

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.database.dtos.UserTeamDto
import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse

class ControllerTest {
    private val pokemonDao = mockk<PokemonDao>()
    private val userDao = mockk<UserDao>()
    private val controller = Controller(pokemonDao, userDao)

    @Test
    fun givenTeamWithSixPokemon_whenAddingAnotherOne_thenDoesNotAddIt() = runBlocking {
        coEvery { userDao.team(1) } returns (1..6).map { pokemonId -> UserTeamDto(1, pokemonId) }

        val added = controller.addPokemonToTeam(7, Session(1))

        assertFalse(added)
        coVerify(exactly = 1) { userDao.team(1) }
        coVerify(exactly = 0) { userDao.addPokemonToTeam(any(), any()) }
    }
}
