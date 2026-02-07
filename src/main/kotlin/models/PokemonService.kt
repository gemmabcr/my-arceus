package dev.gemmabcr.models

import dev.gemmabcr.database.dtos.PokemonDto

interface PokemonService {
    suspend fun readAll(criteria: QueryCriteria): List<PokemonDto>

    suspend fun read(id: Int): PokemonDto
}
