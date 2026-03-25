package dev.gemmabcr.models.pokemons

import dev.gemmabcr.models.pokemons.todo.ProgressToDo

data class Pokemon(
    override val hisuiId: Int,
    override val generalId: Int,
    override val name: String,
    override val types: List<Type>,
    override val toDos: List<ProgressToDo>,
): BasePokemon
