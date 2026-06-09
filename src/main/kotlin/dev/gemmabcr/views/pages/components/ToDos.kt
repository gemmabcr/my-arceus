package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.pokemons.todo.ProgressToDo
import dev.gemmabcr.views.adapters.ToDoTypeAdapter
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.button as actionButton
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import kotlinx.html.ButtonType
import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.TBODY
import kotlinx.html.button
import kotlinx.html.form
import kotlinx.html.input
import kotlinx.html.onClick
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.title
import kotlinx.html.tr

class ToDos(
    private val pokemonId: Int,
    private val toDos: List<ProgressToDo>
) : View {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(align = AlignItems.CENTER, style = "padding: 1rem;") {
            row(JustifyContent.CENTER, AlignItems.CENTER, Gap.MAX) {
                h4(translate(CommonI18nKey.TODOS), margin = false)
                actionButton(
                    translate(CommonI18nKey.EDIT),
                    style = "height: auto; padding: 0.25rem 0.5rem;",
                    onClick = toggleEditScript()
                )
            }
            val isCompleted = toDos.all { it.completed() }
            h5(title(isCompleted), margin = false)
            todosForm(isCompleted)
        }
    }

    private fun FlowContent.todosForm(isCompleted: Boolean) {
        form(action = "/pokemons/$pokemonId/todos", method = FormMethod.post) {
            style = "width: 100%;"
            table {
                if (isCompleted) {
                    style = "display: none;"
                    editorClass("table")
                }
                tbody {
                    tr {
                        style = "background-color: ${Colors.BLUE_GREY}"
                        listOf(CommonI18nKey.PROGRESS, CommonI18nKey.DESCRIPTION).forEach {
                            th {
                                style = "color: ${Colors.DARKEST_BLUE}"
                                +translate(it)
                            }
                        }
                    }
                    toDos.forEach { todoRow(it) }
                }
            }
            row(JustifyContent.START, style = "display: none; margin-top: 0.5rem;") {
                editorClass("flex")
                actionButton(translate(CommonI18nKey.SUBMIT), ButtonType.submit)
            }
        }
    }

    private fun TBODY.todoRow(toDo: ProgressToDo) {
        tr {
            style = "background-color: ${if (toDo.completed()) Colors.CREAM_LIGHEST else Colors.WHITE}"
            td {
                span {
                    readerClass()
                    +numbersText(toDo)
                }
                input(InputType.number, name = "$TODO_PROGRESS_PREFIX${toDo.id}") {
                    value = toDo.done.toString()
                    min = "0"
                    max = toDo.goal.toString()
                    style = "display: none; width: 4rem; padding: 0.35rem;"
                    editorClass("inline-block")
                }
                span {
                    style = "display: none;"
                    editorClass("inline")
                    +" ${translate(CommonI18nKey.OF)} ${toDo.goal}"
                }
                button(type = ButtonType.button) {
                    title = translate(CommonI18nKey.COMPLETED)
                    onClick = "this.parentElement.querySelector('input').value=${toDo.goal}"
                    style =
                        "display: none; margin-left: 0.4rem; border: none; border-radius: 4px; " +
                                "padding: 0.35rem 0.5rem; cursor: pointer;"
                    editorClass("inline-block")
                    +"✓"
                }
            }
            td { +ToDoTypeAdapter(toDo.toDoType).text() }
        }
    }

    private fun CommonAttributeGroupFacade.editorClass(display: String) {
        attributes["class"] = editorClassName()
        attributes["data-edit-display"] = display
    }

    private fun CommonAttributeGroupFacade.readerClass() {
        attributes["class"] = readerClassName()
    }

    private fun toggleEditScript(): String =
        "document.querySelectorAll('.${editorClassName()}').forEach(e=>" +
                "e.style.display=e.style.display==='none'?e.dataset.editDisplay:'none');" +
                "document.querySelectorAll('.${readerClassName()}').forEach(e=>" +
                "e.style.display=e.style.display==='none'?'inline':'none')"

    private fun editorClassName() = "todo-editor-$pokemonId"

    private fun readerClassName() = "todo-reader-$pokemonId"

    private fun title(isCompleted: Boolean): String = when (isCompleted) {
        true -> "\uD83C\uDF89 ${translate(CommonI18nKey.COMPLETED)}!!"
        false -> "${translate(CommonI18nKey.IN_PROGRESS)} (${toDos.count { it.completed() }}/${toDos.size})"
    }

    private fun numbersText(toDo: ProgressToDo): String = when (toDo.completed()) {
        true -> "✔\uFE0F"
        false -> "${toDo.done} ${translate(CommonI18nKey.OF)} ${toDo.goal}"
    }
}

private const val TODO_PROGRESS_PREFIX = "todoProgress_"
