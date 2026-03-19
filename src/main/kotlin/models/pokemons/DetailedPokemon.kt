package dev.gemmabcr.models.pokemons

import dev.gemmabcr.models.pokemons.evolutions.EvolutionChain
import dev.gemmabcr.models.pokemons.todo.ToDo

data class DetailedPokemon(
    override val hisuiId: Int,
    override val generalId: Int,
    override val name: String,
    override val types: List<Type>,
    val location: List<Location>,
    override val toDos:  List<ToDo<*>>,
    val caughtCondition: CaughtCondition?,
    val evolutionChain: List<EvolutionChain>,
): BasePokemon
