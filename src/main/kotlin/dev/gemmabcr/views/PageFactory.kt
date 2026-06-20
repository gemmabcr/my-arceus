package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.ocr.GameScreenshotOcrService
import dev.gemmabcr.ocr.OcrTodoImportService
import dev.gemmabcr.security.SessionTokenService
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

class PageFactory(
    private val controller: Controller,
    private val authDao: AuthDao,
    private val todoProgressService: TodoProgressService,
    private val ocrService: GameScreenshotOcrService,
    private val ocrTodoImportService: OcrTodoImportService,
    private val sessionTokenService: SessionTokenService,
) {
    fun create(application: Application) {
        application.routing {
            staticResources("/icons", "icons")
            route("/") {
                get {
                    call.respondRedirect("pokemons")
                }
            }
            val views = listOf(
                LoginView(authDao, sessionTokenService),
                PokemonsView(controller, todoProgressService, sessionTokenService),
                TeamView(controller, sessionTokenService),
                OcrView(ocrService, ocrTodoImportService, sessionTokenService),
            )
            views.forEach { it.create(application) }

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
}
