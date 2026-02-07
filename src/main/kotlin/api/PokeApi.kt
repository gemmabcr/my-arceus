package dev.gemmabcr.api

import dev.gemmabcr.api.dtos.EntryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class PokemonApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val apiUrl = "https://pokeapi.co/api/v2"

    private fun pokemonUrl(generalId: Int): String = "$apiUrl/pokemon-species/${generalId}"

    suspend fun getPokemon(generalId: Int): EntryDto = client.get(pokemonUrl(generalId)).body<EntryDto>()
}
