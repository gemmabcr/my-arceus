package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.views.pages.DetailView
import dev.gemmabcr.views.pages.ListView
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.uri
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class PokemonsView(
    private val controller: Controller,
    private val todoProgressService: TodoProgressService
) : View {
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
                post("/{id}/todos") {
                    val id = call.parameters["id"]!!.toInt()
                    val parameters = call.receiveParameters()
                    val updates = parameters.names()
                        .filter { it.startsWith(TODO_PROGRESS_PREFIX) }
                        .mapNotNull { name ->
                            val todoId = name.removePrefix(TODO_PROGRESS_PREFIX).toIntOrNull()
                            val done = parameters[name]?.toIntOrNull()
                            if (todoId == null || done == null) null else todoId to done
                        }
                        .toMap()

                    todoProgressService.update(
                        id,
                        updates,
                        createSession()
                    )

                    val referrer = call.request.headers[HttpHeaders.Referrer] ?: "/pokemons/$id"
                    call.respondRedirect(referrer)
                }
            }
        }
    }
}

private const val TODO_PROGRESS_PREFIX = "todoProgress_"
