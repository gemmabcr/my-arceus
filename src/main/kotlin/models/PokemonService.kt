package dev.gemmabcr.models

import dev.gemmabcr.database.dtos.PokemonDto

interface PokemonService {
    suspend fun list(): List<PokemonDto>

    suspend fun get(id: Int): PokemonDto
}
