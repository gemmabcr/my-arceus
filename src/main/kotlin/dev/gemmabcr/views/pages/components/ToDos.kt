package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.pokemons.todo.ProgressToDo
import dev.gemmabcr.views.adapters.ToDoAdapter
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import dev.gemmabcr.views.ui.tables.RowTableConfig
import dev.gemmabcr.views.ui.tables.TableConfig
import dev.gemmabcr.views.ui.tables.table
import kotlinx.html.FlowContent

class ToDos(private val toDos: List<ProgressToDo>) : View {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(align = AlignItems.CENTER, style = "padding: 1rem;") {
            row(JustifyContent.CENTER, AlignItems.CENTER, Gap.MAX) {
                h4(translate(CommonI18nKey.TODOS), margin = false)
                buttonLink("", translate(CommonI18nKey.EDIT))
            }
            val isCompleted = toDos.all { it.completed() }
            h5(title(isCompleted), margin = false)
            if (isCompleted.not()) {
                table(
                    TableConfig(
                        listOf(CommonI18nKey.PROGRESS, CommonI18nKey.DESCRIPTION).map { translate(it) },
                        toDos.map { toDo ->
                            RowTableConfig(
                                if (toDo.completed()) Colors.CREAM_LIGHEST else Colors.WHITE,
                                listOf(numbersText(toDo), ToDoAdapter(toDo.toDo).text())
                            )
                        }
                    )
                )
            }
        }
    }

    private fun title(isCompleted: Boolean): String = when (isCompleted) {
        true -> "$\uD83C\uDF89 ${translate(CommonI18nKey.COMPLETED)}!!"
        false -> "${translate(CommonI18nKey.IN_PROGRESS)} (${toDos.filter { it.completed() }.size}/${toDos.size})"
    }

    private fun numbersText(toDo: ProgressToDo): String = when (toDo.completed()) {
        true -> "✔\uFE0F"
        false -> "${toDo.done} ${translate(CommonI18nKey.OF)} ${toDo.goal}"
    }
}
