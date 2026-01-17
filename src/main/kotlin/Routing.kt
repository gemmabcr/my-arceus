package dev.gemmabcr

import dev.gemmabcr.database.PokemonRepository
import dev.gemmabcr.database.dtos.PokemonDto
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(repo: PokemonRepository) {
    routing {
        get("/") {
            call.respondText("My arceus pokedex")
        }
        route("/pokemons") {
            get {
                val pokemons: List<PokemonDto> = repo.list()
                call.respondText(pokemons.joinToString("\n"))
            }
            get("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val pokemon = repo.get(id)
                call.respondText(pokemon.toString())
            }
        }
    }
}
