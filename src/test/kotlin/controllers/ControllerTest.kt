package controllers

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.database.dtos.PokemonDto
import dev.gemmabcr.database.dtos.ToDoDto
import dev.gemmabcr.database.dtos.UserTeamDto
import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserDao
import dev.gemmabcr.models.pokemons.todo.SearchTask
import dev.gemmabcr.models.pokemons.todo.SearchToDoType
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
    private val todoProgressService = TodoProgressService(pokemonDao, userDao)

    @Test
    fun givenTeamWithSixPokemon_whenAddingAnotherOne_thenDoesNotAddIt() = runBlocking {
        coEvery { userDao.team(1) } returns (1..6).map { pokemonId -> UserTeamDto(1, pokemonId) }

        val added = controller.addPokemonToTeam(7, Session(1))

        assertFalse(added)
        coVerify(exactly = 1) { userDao.team(1) }
        coVerify(exactly = 0) { userDao.addPokemonToTeam(any(), any()) }
    }

    @Test
    fun givenTodoUpdates_whenSavingProgress_thenValidatesTodos() = runBlocking {
        val pokemon = mockk<PokemonDto>()
        coEvery { pokemonDao.pokemon(25) } returns pokemon
        coEvery { pokemon.toDos } returns listOf(
            ToDoDto(1, SearchToDoType(SearchTask.BIDOOF), 10),
            ToDoDto(2, SearchToDoType(SearchTask.BLISSEY), 5),
        )
        coEvery { userDao.saveTodoProgress(any(), any(), any(), any()) } returns Unit

        todoProgressService.update(25, mapOf(1 to 20, 2 to 3, 99 to 4), Session(1))

        coVerify(exactly = 1) { userDao.saveTodoProgress(1, 25, 1, 10) }
        coVerify(exactly = 1) { userDao.saveTodoProgress(1, 25, 2, 3) }
        coVerify(exactly = 0) { userDao.saveTodoProgress(1, 25, 99, any()) }
    }
}
