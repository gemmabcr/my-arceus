package dev.gemmabcr.models.evolutions

data class EvolvesFrom(
    override val name: String,
    override val detail: EvolutionChainDetail,
): EvolutionChain
