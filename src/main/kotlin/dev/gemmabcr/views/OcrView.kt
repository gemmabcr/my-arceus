package dev.gemmabcr.views

import dev.gemmabcr.ocr.GameScreenshotOcrService
import dev.gemmabcr.ocr.OcrMatchedToDo
import dev.gemmabcr.ocr.OcrTodoImportService
import dev.gemmabcr.views.pages.OcrPage
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.response.respondRedirect
import io.ktor.utils.io.core.readBytes

class OcrView(
    private val ocrService: GameScreenshotOcrService,
    private val ocrTodoImportService: OcrTodoImportService
) : View {
    override fun create(application: Application) {
        application.routing {
            route("/ocr") {
                get {
                    call.respondHtmlTemplate(OcrPage()) {}
                }
                post {
                    var imageBytes: ByteArray? = null
                    var originalFileName = "screenshot.png"

                    call.receiveMultipart().forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                if (part.name == "screenshot") {
                                    imageBytes = part.provider().readBytes()
                                    originalFileName = part.originalFileName ?: originalFileName
                                }
                            }

                            else -> Unit
                        }
                        part.dispose()
                    }

                    val result = try {
                        imageBytes?.let { bytes -> ocrService.extractPokedexProgress(bytes, originalFileName) }
                    } catch (exception: IllegalStateException) {
                        call.respondHtmlTemplate(OcrPage(error = exception.message)) {}
                        return@post
                    }

                    when {
                        result == null -> call.respondHtmlTemplate(
                            OcrPage(error = "Need image to execute OCR.")
                        ) {}

                        else -> {
                            val preview = ocrTodoImportService.buildPreview(result, createSession())
                            call.respondHtmlTemplate(OcrPage(result = result, importPreview = preview)) {}
                        }
                    }
                }
            }

            route("/ocr/import") {
                post {
                    val parameters = call.receiveParameters()
                    val pokemonId = parameters["pokemonId"]?.toIntOrNull()
                    val updates = parameters.getAll("todoUpdate").orEmpty().mapNotNull(::toMatchedTodo)

                    when {
                        pokemonId == null || updates.isEmpty() -> call.respondRedirect("/ocr")
                        else -> {
                            ocrTodoImportService.importToDos(pokemonId, updates, createSession())
                            call.respondRedirect("/pokemons/$pokemonId")
                        }
                    }
                }
            }
        }
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

private const val OCR_TODO_PARTS = 5
private const val TODO_UPDATE_SEPARATOR = "::"
private const val TODO_ID_INDEX = 0
private const val TODO_DONE_INDEX = 1
private const val TODO_GOAL_INDEX = 2
private const val TODO_LABEL_INDEX = 3
private const val TODO_EXTRACTED_LABEL_INDEX = 4
