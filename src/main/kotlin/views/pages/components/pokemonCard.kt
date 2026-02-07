package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.views.components.toDos
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.grid
import dev.gemmabcr.views.ui.h3
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.style

fun FlowContent.pokemonCard(
    pokemon: Pokemon,
    leftColumnItem: (FlowContent.() -> Unit)? = null,
    rightColumn: (FlowContent.() -> Unit)? = null,
) {
    column {
        style =
            "background-color: #F2F0E3; border-radius: 0 0 1rem 1rem; box-shadow: rgba(0, 0, 0, 0.16) 0 1px 4px; width: 100%;"
        grid("240px 1fr", style = "padding: 2rem;") {
            pokemonBaseInfo(pokemon) {
                leftColumnItem?.let { it() }
            }
            when (rightColumn) {
                null -> {
                    toDos(pokemon)
                }

                else -> {
                    rightColumn()
                }
            }
        }
        if (rightColumn != null) {
            toDos(pokemon)
        }
    }
}

private fun FlowContent.pokemonBaseInfo(pokemon: Pokemon, block: DIV.() -> Unit = {}) {
    column(align = AlignItems.CENTER) {
        h3("#${pokemon.hisuiId} ${pokemon.name}", margin = false)
        pokemonImage(pokemon.generalId)
        typeChips(pokemon.types.map { it.name.lowercase() to it.text })
        block()
    }
}

private fun DIV.toDos(pokemon: Pokemon) {
    toDos(pokemon.toDos)
}
