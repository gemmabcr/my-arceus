package dev.gemmabcr.views.pages

import dev.gemmabcr.ocr.OcrTodoImportPreview
import dev.gemmabcr.ocr.PokedexScreenshotOcrResult
import dev.gemmabcr.models.Session
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.h4
import kotlinx.html.DIV
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.hiddenInput
import kotlinx.html.input
import kotlinx.html.p
import kotlinx.html.style

class OcrPage(
    private val result: PokedexScreenshotOcrResult? = null,
    private val importPreview: OcrTodoImportPreview? = null,
    private val error: String? = null,
    private val session: Session = Session(),
) : HtmlLayout(CommonI18nKey.LIST, session) {
    private val ocrDescription =
        "Puja una pantalla de tasques de la Pokédex i intentarem extreure el Pokémon, " +
                "el número, el progrés i les files visibles."

    override fun DIV.content() {
        column(gap = Gap.MAX) {
            row(style = "flex-wrap: wrap;") {
                buttonLink("/pokemons", "Back to pokedex")
            }

            column(
                gap = Gap.MIN,
                style = "background-color: ${Colors.CREAM_LIGHEST}; padding: 1rem; border-radius: 1rem;"
            ) {
                h4("Importa una captura del joc", margin = false)
                p {
                    style = "margin: 0; color: ${Colors.DARK_BLUE};"
                    +ocrDescription
                }
                form(
                    action = "/ocr",
                    method = FormMethod.post,
                    encType = FormEncType.multipartFormData
                ) {
                    style = "display: flex; gap: 1rem; flex-wrap: wrap; align-items: center;"
                    input(type = InputType.file, name = "screenshot") {
                        accept = ".png,.jpg,.jpeg,.webp"
                    }
                    button {
                        style =
                            "background-color: ${Colors.CREAM}; border: none; padding: 0.6rem 1.2rem; " +
                                    "border-radius: 8px; cursor: pointer;"
                        +"Executa OCR"
                    }
                }
            }

            error?.let { message ->
                messageCard("Error", message, Colors.CREAM)
            }

            result?.warning?.let { warning ->
                messageCard("Avís", warning, Colors.BLUE_GREY)
            }

            result?.let { ocrResult ->
                row(style = "flex-wrap: wrap; gap: 0.75rem;") {
                    ocrResult.pokemonName?.let { statCard("Pokemon", it) }
                    ocrResult.pokemonNumber?.let { statCard("Numero", it.toString()) }
                    ocrResult.progressLevel?.let { statCard("Progres", it.toString()) }
                }

                if (ocrResult.tasks.isNotEmpty()) {
                    tasksColumns(ocrResult)
                }

                importPreview?.let { preview ->
                    importSection(preview)
                }
            }
        }
    }

    private fun DIV.tasksColumns(ocrResult: PokedexScreenshotOcrResult) {
        column(
            gap = Gap.MIN,
            style = "background-color: white; padding: 1rem; border-radius: 1rem;"
        ) {
            h4("Tasques detectades", margin = false)
            column(gap = Gap.MIN) {
                ocrResult.tasks.forEach { task ->
                    row(
                        style =
                            "justify-content: space-between; padding: 0.6rem 0.8rem; border-radius: 0.75rem;" +
                                    " background-color: ${Colors.CREAM_LIGHEST};"
                    ) {
                        p {
                            style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                            +task.label
                        }
                        p {
                            style = "margin: 0; font-weight: bold; color: ${Colors.DARK_BLUE};"
                            +task.value.toString()
                        }
                    }
                }
            }
        }
    }

    private fun DIV.importSection(preview: OcrTodoImportPreview) {
        column(
            gap = Gap.MIN,
            style = "background-color: white; padding: 1rem; border-radius: 1rem;"
        ) {
            h4("Importar a base de dades", margin = false)
            preview.pokemonName?.let { name ->
                p {
                    style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                    +"Pokemon detectat: $name"
                }
            }

            if (preview.matchedToDos.isNotEmpty()) {
                p {
                    style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                    +"S'han pogut relacionar ${preview.matchedToDos.size} tasques amb la base de dades."
                }
                form(action = "/ocr/import", method = FormMethod.post) {
                    style = "display: flex; flex-direction: column; gap: 0.75rem; align-items: flex-start;"
                    hiddenInput(name = "pokemonId") {
                        value = preview.pokemonId?.toString() ?: ""
                    }
                    preview.matchedToDos.forEach { todo ->
                        hiddenInput(name = "todoUpdate") {
                            value = listOf(
                                todo.todoId.toString(),
                                todo.done.toString(),
                                todo.goal.toString(),
                                todo.label,
                                todo.extractedLabel
                            ).joinToString(TODO_UPDATE_SEPARATOR)
                        }
                    }
                    button {
                        style =
                            "background-color: ${Colors.DARK_BLUE}; color: ${Colors.ON_DARK_BLUE}; border: none; " +
                                    "padding: 0.6rem 1.2rem; border-radius: 8px; cursor: pointer;"
                        +"Guardar progressos"
                    }
                }
            }

            if (preview.unmatchedTasks.isNotEmpty()) {
                p {
                    style = "margin: 0; color: ${Colors.DARK_BLUE};"
                    +"Tasques sense relacio automatica:"
                }
                column(gap = Gap.MIN) {
                    preview.unmatchedTasks.forEach { task ->
                        p {
                            style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                            +"- ${task.label}: ${task.value}"
                        }
                    }
                }
            }
        }
    }

    private fun DIV.messageCard(title: String, message: String, color: Colors) {
        column(
            gap = Gap.MIN,
            style = "background-color: $color; padding: 1rem; border-radius: 1rem;"
        ) {
            h4(title, margin = false)
            p {
                style = "margin: 0; color: ${Colors.DARKEST_BLUE};"
                +message
            }
        }
    }

    private fun DIV.statCard(label: String, value: String) {
        column(
            gap = Gap.MIN,
            style =
                "min-width: 110px; background-color: ${Colors.DARK_BLUE}; color: ${Colors.ON_DARK_BLUE};" +
                        " padding: 0.75rem 1rem; border-radius: 0.75rem;"
        ) {
            p {
                style = "margin: 0; font-size: 0.85rem; color: ${Colors.BLUE_GREY};"
                +label
            }
            p {
                style = "margin: 0; font-size: 1.6rem; font-weight: bold;"
                +value
            }
        }
    }
}

private const val TODO_UPDATE_SEPARATOR = "::"
