package dev.gemmabcr.api

import dev.gemmabcr.api.dtos.PokedexResponse
import dev.gemmabcr.api.dtos.EntryDto
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
    private val apiUrl = "https://pokeapi.co/api/v2"

    private lateinit var pokedex: PokedexResponse
    private lateinit var entries: Map<Int, EntryDto>
    private lateinit var pokemonList: List<PokemonEntry>

    init {
        runBlocking {
            if (::pokedex.isInitialized.not()) {
                try {
                    pokedex = client.get("$apiUrl/pokedex/30/").body()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (::entries.isInitialized.not()) {
                try {
                    entries = pokedex.pokemon_entries.associate {
                        val response = client.get(it.pokemon_species.url).body<EntryDto>()
                        it.entry_number to response
                    }
                    pokemonList = pokedex.pokemon_entries.map { entry ->
                        require(entries.containsKey(entry.entry_number)) { "entries not contain ${entry.pokemon_species.name}" }
                        PokemonEntry(
                            entry.entry_number,
                            entry.pokemon_species.name,
                            entries[entry.entry_number]!!.id,
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun pokemons(): List<PokemonEntry> = pokemonList
}
