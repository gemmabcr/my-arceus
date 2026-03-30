package dev.gemmabcr.models.pokemons.evolutions

data class EvolvesTo(
    override val name: String,
    override val detail: EvolutionChainDetail,
): EvolutionChain
