package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.BasePokemon
import dev.gemmabcr.models.ToDo
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.grid
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import dev.gemmabcr.views.ui.table
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.style

fun FlowContent.pokemonCard(
    pokemon: BasePokemon,
    block: (FlowContent.() -> Unit)? = null,
) {
    column {
        style =
            "background-color: ${Colors.CREAM}; border-radius: 0 0 1rem 1rem; box-shadow: rgba(0, 0, 0, 0.16) 0 1px 4px; width: 100%;"
        row(align = AlignItems.CENTER, style = "background-color: ${Colors.DARK_BLUE}; padding: 0.25rem;") {
            div {
                style = "outline: 1px solid ${Colors.CREAM}; padding: 0.25rem;"
                h4 {
                    style = textStyle()
                    +pokemon.hisuiId.toString()
                }
            }
            h3 {
                style = textStyle()
                +pokemon.name
            }
            row { style = "width: 100%;" }
            typeChips(pokemon.types.map { it.name.lowercase() to it.toString() })
        }
        grid("240px 1fr") {
            column(JustifyContent.CENTER, AlignItems.CENTER, style = "padding: 1rem") {
                pokemonImage(pokemon.generalId)
            }
            toDos(pokemon.toDos)
        }
        block?.let { it() }
    }
}

private fun FlowContent.toDos(items: List<ToDo>) {
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

private fun textStyle(): String = "display: flex; margin: 0; color: ${Colors.ON_DARK_BLUE};"
