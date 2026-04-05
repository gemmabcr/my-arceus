package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class TeamView(private val controller: Controller) : View {
    override fun create(application: Application) {
        application.routing {
            route("/team") {
                post {
                    val formParameters = call.receiveParameters()
                    val pokemonId = formParameters["pokemonId"]?.toInt()
                    val action = formParameters["action"]
                    val redirectTo = formParameters["redirectTo"] ?: "/pokemons"

                    when {
                        pokemonId == null -> call.respondRedirect(redirectTo)
                        action == "remove" -> {
                            controller.removePokemonFromTeam(pokemonId, createSession())
                            call.respondRedirect(redirectTo)
                        }

                        else -> {
                            controller.addPokemonToTeam(pokemonId, createSession())
                            call.respondRedirect(redirectTo)
                        }
                    }
                }
            }
        }
    }
}
