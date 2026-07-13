package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.views.pages.DetailView
import dev.gemmabcr.views.pages.ListView
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.text.toInt

class PokemonsView(
    private val controller: Controller,
    private val todoProgressService: TodoProgressService,
    private val sessionTokenService: SessionTokenService,
) : View {
    override fun create(application: Application) {
        application.routing {
            route("/pokemons") {
                get {
                    val parameters: Parameters = call.request.queryParameters
                    val todos = controller.toDos()
                    val criteria = QueryCriteriaBuilder().with(todos).with(parameters).build()
                    val session = call.createSession(sessionTokenService)
                    val result = controller.pokemons(criteria, session)
                    val team = controller.team(session)
                    call.applyLocale()
                    call.respondHtmlTemplate(ListView(criteria, result, todos, team, call.request.uri, session)) {}
                }
                get("/{id}") {
                    val id = call.parameters["id"]!!.toInt()
                    val session = call.createSession(sessionTokenService)
                    val pokemon = controller.pokemon(id, session)
                    call.applyLocale()
                    call.respondHtmlTemplate(DetailView(pokemon, session)) {}
                }
                post("/{id}/todos") {
                    val session = call.createSession(sessionTokenService)
                    if (session.user == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@post
                    }
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
                        session
                    )

                    val referrer = call.request.headers[HttpHeaders.Referrer] ?: "/pokemons/$id"
                    call.respondRedirect(referrer)
                }
            }
        }
    }
}

private const val TODO_PROGRESS_PREFIX = "todoProgress_"
