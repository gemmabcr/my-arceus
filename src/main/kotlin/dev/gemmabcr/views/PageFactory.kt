package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.models.Session
import dev.gemmabcr.views.pages.DetailView
import dev.gemmabcr.views.pages.ListView
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.uri
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class PageFactory(private val controller: Controller) {
    fun create(application: Application) {
        application.routing {
            staticResources("/icons", "icons")
            route("/") {
                get {
                    call.respondRedirect("pokemons")
                }
            }
            route("/pokemons") {
                get {
                    val parameters: Parameters = call.request.queryParameters
                    val todos = controller.toDos()
                    val criteria = QueryCriteriaBuilder().with(todos).with(parameters).build()
                    val session = createSession()
                    val result = controller.pokemons(criteria, session)
                    val team = controller.team(session)
                    call.respondHtmlTemplate(ListView(criteria, result, todos, team, call.request.uri)) {}
                }
                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val pokemon = controller.pokemon(id, createSession())
                    call.respondHtmlTemplate(DetailView(pokemon)) {}
                }
            }
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
            route("/lang/{locale}") {
                get {
                    val locale = call.parameters["locale"] ?: "en"
                    call.response.cookies.append("lang", locale, path = "/")
                    val referrer = call.request.headers[HttpHeaders.Referrer] ?: "/pokemons"
                    call.respondRedirect(referrer)
                }
            }
        }
    }

    private fun createSession(): Session = Session(1) // TODO
}
