package dev.gemmabcr.views.ui

import dev.gemmabcr.ocr.OcrTodoImportPreview
import dev.gemmabcr.ocr.OcrWarning
import dev.gemmabcr.ocr.PokedexScreenshotOcrResult
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h3
import kotlinx.html.hiddenInput
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.p
import kotlinx.html.span

class OcrContent(
    private val result: PokedexScreenshotOcrResult?,
    private val importPreview: OcrTodoImportPreview?,
    private val error: String?,
    private val isLoggedIn: Boolean,
) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(gap = Gap.MAX) {
            classes = setOf("ocr-layout")
            uploadCard()
            error?.let {
                val translatedMessage = it.ocrErrorKey()?.let(::translate) ?: it
                messageCard(translate(CommonI18nKey.ERROR), translatedMessage, "ocr-message-error")
            }
            result?.warning?.let { warnings ->
                val message = warnings.joinToString(" ") { translate(it.i18nKey()) }
                messageCard(translate(CommonI18nKey.WARNING), message, "ocr-message-warning")
            }
            result?.let { ocrResult ->
                stats(ocrResult)
                if (ocrResult.tasks.isNotEmpty()) tasksCard(ocrResult)
                importPreview?.let { importCard(it) }
            }
        }
    }

    private fun DIV.uploadCard() {
        div {
            classes = setOf("ui-card", "ocr-card")
            cardHeader(translate(CommonI18nKey.OCR_UPLOAD_TITLE))
            column(gap = Gap.MAX) {
                classes = setOf("ocr-card-body")
                if (!isLoggedIn) {
                    LoginRequiredNotice(CommonI18nKey.LOGIN_REQUIRED_PROGRESS).create(this)
                }
                p {
                    classes = setOf("ui-muted", "ocr-description")
                    +translate(CommonI18nKey.OCR_DESCRIPTION)
                }
                uploadForm()
            }
        }
    }

    private fun DIV.uploadForm() {
        val selectScreenshot = translate(CommonI18nKey.OCR_SELECT_SCREENSHOT)
        val fallbackFileName = selectScreenshot.jsSingleQuoted()
        form(action = "/ocr", method = FormMethod.post, encType = FormEncType.multipartFormData) {
            classes = if (isLoggedIn) setOf("ocr-upload-form") else setOf("ocr-upload-form", "ocr-form-disabled")
            label {
                classes = setOf("ui-field", "ocr-file-picker")
                input(type = InputType.file, name = "screenshot") {
                    accept = ".png,.jpg,.jpeg,.webp"
                    disabled = !isLoggedIn
                    classes = setOf("ocr-file-input")
                    attributes["aria-label"] = selectScreenshot
                    attributes["onchange"] =
                        "this.nextElementSibling.textContent=" +
                        "this.files[0]?.name||'$fallbackFileName'"
                }
                span { +selectScreenshot }
            }
            button {
                classes = setOf("ui-primary-button", "ocr-submit-button")
                disabled = !isLoggedIn
                +translate(CommonI18nKey.OCR_RUN)
            }
        }
    }

    private fun DIV.stats(ocrResult: PokedexScreenshotOcrResult) {
        div {
            classes = setOf("ocr-stats")
            ocrResult.pokemonName?.let { statCard(translate(CommonI18nKey.OCR_STAT_POKEMON), it) }
            ocrResult.pokemonNumber?.let { statCard(translate(CommonI18nKey.NUMBER), it.toString()) }
            ocrResult.progressLevel?.let { statCard(translate(CommonI18nKey.PROGRESS), it.toString()) }
        }
    }

    private fun DIV.tasksCard(ocrResult: PokedexScreenshotOcrResult) {
        div {
            classes = setOf("ui-card", "ocr-card")
            cardHeader(translate(CommonI18nKey.OCR_DETECTED_TASKS))
            column(gap = Gap.MIN) {
                classes = setOf("ocr-card-body")
                ocrResult.tasks.forEach { task ->
                    div {
                        classes = setOf("ui-panel", "ocr-task-row")
                        p { +task.label }
                        p {
                            classes = setOf("ocr-task-value")
                            +task.value.toString()
                        }
                    }
                }
            }
        }
    }

    private fun DIV.importCard(preview: OcrTodoImportPreview) {
        div {
            classes = setOf("ui-card", "ocr-card")
            cardHeader(translate(CommonI18nKey.OCR_DATABASE_IMPORT))
            column(gap = Gap.MIN) {
                classes = setOf("ocr-card-body")
                preview.pokemonName?.let { name ->
                    p { +translate(CommonI18nKey.OCR_DETECTED_POKEMON).replace("{name}", name) }
                }
                if (preview.matchedToDos.isNotEmpty()) matchedTasks(preview)
                if (preview.unmatchedTasks.isNotEmpty()) unmatchedTasks(preview)
            }
        }
    }

    private fun DIV.matchedTasks(preview: OcrTodoImportPreview) {
        p {
            +translate(CommonI18nKey.OCR_IMPORT_MATCHED_TASKS)
                .replace("{count}", preview.matchedToDos.size.toString())
        }
        form(action = "/ocr/import", method = FormMethod.post) {
            classes = setOf("ocr-import-form")
            hiddenInput(name = "pokemonId") { value = preview.pokemonId?.toString() ?: "" }
            preview.matchedToDos.forEach { todo ->
                hiddenInput(name = "todoUpdate") {
                    value = listOf(
                        todo.todoId.toString(),
                        todo.done.toString(),
                        todo.goal.toString(),
                        todo.label,
                        todo.extractedLabel,
                    ).joinToString(TODO_UPDATE_SEPARATOR)
                }
            }
            button {
                classes = setOf("ui-primary-button")
                +translate(CommonI18nKey.OCR_SAVE_PROGRESS)
            }
        }
    }

    private fun DIV.unmatchedTasks(preview: OcrTodoImportPreview) {
        p {
            classes = setOf("ui-muted")
            +translate(CommonI18nKey.OCR_UNMATCHED_TASKS)
        }
        column(gap = Gap.MIN) {
            preview.unmatchedTasks.forEach { task ->
                p {
                    classes = setOf("ocr-unmatched-task")
                    +"${task.label}: ${task.value}"
                }
            }
        }
    }

    private fun DIV.statCard(label: String, value: String) {
        div {
            classes = setOf("ui-card", "ocr-stat-card")
            p {
                classes = setOf("ocr-stat-label")
                +label
            }
            p {
                classes = setOf("ocr-stat-value")
                +value
            }
        }
    }

}

private fun DIV.cardHeader(title: String) {
    div {
        classes = setOf("ocr-card-header")
        h3 { +title }
    }
}

private fun DIV.messageCard(title: String, message: String, toneClass: String) {
    div {
        classes = setOf("ui-card", "ocr-message", toneClass)
        h3 { +title }
        p { +message }
    }
}

private fun OcrWarning.i18nKey(): CommonI18nKey = when (this) {
    OcrWarning.MISSING_NAME -> CommonI18nKey.OCR_WARNING_MISSING_NAME
    OcrWarning.MISSING_NUMBER -> CommonI18nKey.OCR_WARNING_MISSING_NUMBER
    OcrWarning.MISSING_PROGRESS -> CommonI18nKey.OCR_WARNING_MISSING_PROGRESS
    OcrWarning.MISSING_TASKS -> CommonI18nKey.OCR_WARNING_MISSING_TASKS
}

private fun String.jsSingleQuoted(): String = replace("\\", "\\\\").replace("'", "\\'")

private fun String.ocrErrorKey(): CommonI18nKey? = when (this) {
    OCR_EXECUTION_FAILED -> CommonI18nKey.OCR_EXECUTION_FAILED
    OCR_IMAGE_REQUIRED -> CommonI18nKey.OCR_IMAGE_REQUIRED
    else -> null
}

private const val OCR_EXECUTION_FAILED = "OCR execution failed."
private const val OCR_IMAGE_REQUIRED = "Need image to execute OCR."
private const val TODO_UPDATE_SEPARATOR = "::"
