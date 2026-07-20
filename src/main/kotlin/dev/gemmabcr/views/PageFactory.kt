package dev.gemmabcr.views

import dev.gemmabcr.controllers.Controller
import dev.gemmabcr.controllers.TodoProgressService
import dev.gemmabcr.models.AuthDao
import dev.gemmabcr.models.Language
import dev.gemmabcr.ocr.GameScreenshotOcrService
import dev.gemmabcr.ocr.OcrTodoImportService
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.security.OAuthService
import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

@Suppress("LongParameterList")
class PageFactory(
    private val controller: Controller,
    private val authDao: AuthDao,
    private val todoProgressService: TodoProgressService,
    private val ocrService: GameScreenshotOcrService,
    private val ocrTodoImportService: OcrTodoImportService,
    private val sessionTokenService: SessionTokenService,
    private val oauthService: OAuthService,
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
                LoginView(authDao, sessionTokenService, oauthService),
                PokemonsView(controller, todoProgressService, sessionTokenService),
                TeamView(controller, sessionTokenService),
                OcrView(ocrService, ocrTodoImportService, sessionTokenService),
            )
            views.forEach { it.create(application) }

            route("/lang/{locale}") {
                get {
                    val language = Language.fromTag(call.parameters["locale"]) ?: Language.EN
                    call.response.cookies.append(
                        name = LANGUAGE_COOKIE,
                        value = language.tag,
                        path = "/",
                        maxAge = LANGUAGE_COOKIE_MAX_AGE_SECONDS,
                    )
                    val referrer = call.request.headers[HttpHeaders.Referrer] ?: "/pokemons"
                    call.respondRedirect(referrer)
                }
            }
        }
    }
}

private const val LANGUAGE_COOKIE_MAX_AGE_SECONDS = 31_536_000L
