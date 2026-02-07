package dev.gemmabcr.views.components

import dev.gemmabcr.models.ToDo
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.table
import kotlinx.html.FlowContent
import kotlin.collections.map

fun FlowContent.toDos(items: List<ToDo>) {
    column(align = AlignItems.CENTER, style = "padding: 1rem;") {
        row(JustifyContent.CENTER, AlignItems.CENTER, Gap.MAX) {
            h4("Tareas de la Pokédex", margin = false)
            buttonLink("", "Editar")
        }
        h5("En progreso (0/${items.size})", margin = false)
        table(
            listOf("Progreso", "Descripción"),
            items.map {
                listOf(
                    "0 of ${it.goal}",
                    it.description
                )
            }
        )
    }
}
