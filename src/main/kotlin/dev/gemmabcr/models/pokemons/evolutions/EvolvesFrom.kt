package dev.gemmabcr.models.pokemons.evolutions

data class EvolvesFrom(
    override val name: String,
    override val detail: EvolutionChainDetail,
): EvolutionChain
