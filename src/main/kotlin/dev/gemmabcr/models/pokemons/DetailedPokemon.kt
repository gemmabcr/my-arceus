package dev.gemmabcr.models.pokemons

import dev.gemmabcr.models.pokemons.evolutions.EvolutionChain
import dev.gemmabcr.models.pokemons.todo.ProgressToDo

data class DetailedPokemon(
    override val hisuiId: Int,
    override val generalId: Int,
    override val name: String,
    override val types: List<Type>,
    val location: List<Location>,
    override val toDos:  List<ProgressToDo>,
    val caughtCondition: CaughtCondition?,
    val evolutionChain: List<EvolutionChain>,
): BasePokemon
