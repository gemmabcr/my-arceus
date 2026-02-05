package dev.gemmabcr.views

import dev.gemmabcr.api.PokemonApi
import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.models.PokemonService
import dev.gemmabcr.views.pages.listView
import dev.gemmabcr.views.pages.detailView
import dev.gemmabcr.views.ui.htmlLayout
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondRedirect
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
            staticResources("/icons", "icons")
            route("/") {
                get {
                    call.respondRedirect("pokemons")
                }
            }
            route("/pokemons") {
                get {
                    val parameters: Parameters = call.request.queryParameters
                    val criteria = QueryCriteriaBuilder().with(parameters).build()
                    val pokemons = controller.pokemons(criteria)
                    call.respondHtml {
                        htmlLayout {
                            listView(criteria, pokemons)
                        }
                    }
                }
                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val pokemon = controller.pokemon(id)
                    call.respondHtml {
                        htmlLayout {
                            detailView(pokemon)
                        }
                    }
                }
            }
        }
    }
}
