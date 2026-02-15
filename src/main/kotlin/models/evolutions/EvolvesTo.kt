package dev.gemmabcr.models.evolutions

data class EvolvesTo(
    override val name: String,
    override val detail: EvolutionChainDetail,
): EvolutionChain
