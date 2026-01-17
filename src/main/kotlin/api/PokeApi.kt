package dev.gemmabcr.api

import dev.gemmabcr.api.dtos.PokedexResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import kotlinx.serialization.json.Json
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking

class PokemonApi {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private lateinit var pokemonNames: List<PokemonEntry>

    init {
        runBlocking {
            if (::pokemonNames.isInitialized.not()) {
                try {
                    val response: PokedexResponse = client.get("https://pokeapi.co/api/v2/pokedex/30/").body()
                    pokemonNames = response.pokemon_entries.mapIndexed { index, entry ->
                        PokemonEntry(
                            index + 1,
                            entry.pokemon_species.name,
                            entry.pokemon_species.url
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun entries(): List<PokemonEntry> = pokemonNames
}
