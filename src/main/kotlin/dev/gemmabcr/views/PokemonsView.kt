package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.views.pages.DetailView
import dev.gemmabcr.views.pages.ListView
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class PokemonsView(private val controller: Controller) : View {
    override fun create(application: Application) {
        application.routing {
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
        }
    }
}
