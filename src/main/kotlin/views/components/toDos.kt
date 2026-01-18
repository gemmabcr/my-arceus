package dev.gemmabcr.views.components

import dev.gemmabcr.models.ToDo
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import dev.gemmabcr.views.ui.table
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.style
import kotlin.collections.map

fun FlowContent.toDos(items: List<ToDo>) {
    div {
        style =
            "display: flex; align-items: center; border-top: 1px solid #D8D2AB; flex-direction: column; gap: 0.5rem; padding: 1rem;"
        h4("Tareas de la Pokédex", margin = false)
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
