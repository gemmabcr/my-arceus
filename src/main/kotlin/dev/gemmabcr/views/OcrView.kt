package dev.gemmabcr.views

import dev.gemmabcr.ocr.GameScreenshotOcrService
import dev.gemmabcr.ocr.OcrMatchedToDo
import dev.gemmabcr.ocr.OcrTodoImportService
import dev.gemmabcr.models.Session
import dev.gemmabcr.security.SessionTokenService
import dev.gemmabcr.views.pages.OcrPage
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.utils.io.core.readBytes

class OcrView(
    private val ocrService: GameScreenshotOcrService,
    private val ocrTodoImportService: OcrTodoImportService,
    private val sessionTokenService: SessionTokenService,
) : View {
    override fun create(application: Application) {
        application.routing {
            ocrRoutes()
            importRoutes()
        }
    }

    private fun Route.ocrRoutes() {
        route("/ocr") {
            get {
                val session = call.createSession(sessionTokenService)
                if (session.user == null) {
                    call.respondRedirect("/login")
                    return@get
                }
                call.applyLocale()
                call.respondHtmlTemplate(OcrPage(session = session)) {}
            }
            post {
                val session = call.requireSession() ?: return@post
                call.respondOcrResult(session, call.receiveScreenshot())
            }
        }
    }

    private fun Route.importRoutes() {
        route("/ocr/import") {
            post {
                val session = call.requireSession() ?: return@post
                val parameters = call.receiveParameters()
                val pokemonId = parameters["pokemonId"]?.toIntOrNull()
                val updates = parameters.getAll("todoUpdate").orEmpty().mapNotNull(::toMatchedTodo)

                when {
                    pokemonId == null || updates.isEmpty() -> call.respondRedirect("/ocr")
                    else -> {
                        ocrTodoImportService.importToDos(pokemonId, updates, session)
                        call.respondRedirect("/pokemons/$pokemonId")
                    }
                }
            }
        }
    }

    private suspend fun ApplicationCall.requireSession(): Session? {
        val session = createSession(sessionTokenService)
        if (session.user == null) {
            respond(HttpStatusCode.Unauthorized)
            return null
        }
        return session
    }

    private suspend fun ApplicationCall.receiveScreenshot(): UploadedScreenshot {
        var screenshot = UploadedScreenshot()
        receiveMultipart().forEachPart { part ->
            if (part is PartData.FileItem && part.name == "screenshot") {
                screenshot = UploadedScreenshot(
                    imageBytes = part.provider().readBytes(),
                    originalFileName = part.originalFileName ?: screenshot.originalFileName,
                )
            }
            part.dispose()
        }
        return screenshot
    }

    private suspend fun ApplicationCall.respondOcrResult(
        session: Session,
        screenshot: UploadedScreenshot,
    ) {
        val result = try {
            screenshot.imageBytes?.let { bytes ->
                ocrService.extractPokedexProgress(bytes, screenshot.originalFileName)
            }
        } catch (exception: IllegalStateException) {
            applyLocale()
            respondHtmlTemplate(OcrPage(error = exception.message, session = session)) {}
            return
        }

        if (result == null) {
            applyLocale()
            respondHtmlTemplate(OcrPage(error = "Need image to execute OCR.", session = session)) {}
            return
        }

        val preview = ocrTodoImportService.buildPreview(result, session)
        applyLocale()
        respondHtmlTemplate(OcrPage(result = result, importPreview = preview, session = session)) {}
    }

    private fun toMatchedTodo(value: String): OcrMatchedToDo? {
        val parts = value.split(TODO_UPDATE_SEPARATOR)
        val todoId = parts.getOrNull(TODO_ID_INDEX)?.toIntOrNull()
        val done = parts.getOrNull(TODO_DONE_INDEX)?.toIntOrNull()
        val goal = parts.getOrNull(TODO_GOAL_INDEX)?.toIntOrNull()
        val label = parts.getOrNull(TODO_LABEL_INDEX)
        val extractedLabel = parts.getOrNull(TODO_EXTRACTED_LABEL_INDEX)

        return when {
            parts.size != OCR_TODO_PARTS -> null
            todoId == null || done == null || goal == null -> null
            label == null || extractedLabel == null -> null
            else -> OcrMatchedToDo(todoId, label, extractedLabel, done, goal)
        }
    }
}

private data class UploadedScreenshot(
    val imageBytes: ByteArray? = null,
    val originalFileName: String = "screenshot.png",
)

private const val OCR_TODO_PARTS = 5
private const val TODO_UPDATE_SEPARATOR = "::"
private const val TODO_ID_INDEX = 0
private const val TODO_DONE_INDEX = 1
private const val TODO_GOAL_INDEX = 2
private const val TODO_LABEL_INDEX = 3
private const val TODO_EXTRACTED_LABEL_INDEX = 4
