package dev.gemmabcr.models

import dev.gemmabcr.models.evolutions.EvolutionChain

data class DetailedPokemon(
    override val hisuiId: Int,
    override val generalId: Int,
    override val name: String,
    override val types: List<Type>,
    val location: List<Location>,
    override val toDos: List<ToDo>,
    val specialCondition: SpecialCondition?,
    val evolutionChain: List<EvolutionChain>,
): BasePokemon
