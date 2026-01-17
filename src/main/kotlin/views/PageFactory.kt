package dev.gemmabcr.views

import dev.gemmabcr.api.PokemonApi
import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.views.pages.pokedexView
import dev.gemmabcr.views.pages.pokemonView
import dev.gemmabcr.views.ui.htmlLayout
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class PageFactory(
    api: PokemonApi,
    service: PokemonService
) {
    private val controller = Controller(api, service)

    fun create(application: Application) {
        application.routing {
            route("/") {
                get {
                    val pokemons = controller.pokemons()
                    call.respondHtml {
                        htmlLayout {
                            pokedexView(pokemons)
                        }
                    }
                }
                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val pokemon = controller.pokemon(id)
                    call.respondHtml {
                        htmlLayout {
                            pokemonView(pokemon)
                        }
                    }
                }
            }
        }
    }
}
