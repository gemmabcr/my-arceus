package dev.gemmabcr.controllers

import dev.gemmabcr.models.PokemonDao
import dev.gemmabcr.models.Session
import dev.gemmabcr.models.UserDao

class TodoProgressService(
    private val pokemonDao: PokemonDao,
    private val userDao: UserDao
) {
    suspend fun update(
        pokemonId: Int,
        updates: Map<Int, Int>,
        session: Session
    ) {
        val userId = session.user ?: return
        val goals = pokemonDao.pokemon(pokemonId).toDos.associate { it.id to it.goal }
        val validatedUpdates = updates.mapNotNull { (todoId, done) ->
            goals[todoId]?.let { goal -> todoId to done.coerceIn(0, goal) }
        }.toMap()

        validatedUpdates.forEach { (todoId, done) ->
            userDao.saveTodoProgress(userId, pokemonId, todoId, done)
        }
    }
}
