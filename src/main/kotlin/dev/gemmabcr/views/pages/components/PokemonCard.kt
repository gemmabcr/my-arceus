package dev.gemmabcr.views.pages.components

import dev.gemmabcr.models.pokemons.BasePokemon
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.grid
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.style

class PokemonCard(
    private val pokemon: BasePokemon,
) : View {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column {
            style =
                "background-color: ${Colors.CREAM}; border-radius: 0 0 1rem 1rem; " +
                        "box-shadow: rgba(0, 0, 0, 0.16) 0 1px 4px; width: 100%;"
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
                TypeChips(pokemon.types).create(this)
            }
            grid("240px 1fr") {
                column(JustifyContent.CENTER, AlignItems.CENTER, style = "padding: 1rem") {
                    PokemonImage(pokemon.generalId).create(this)
                }
                ToDos(pokemon.toDos).create(this)
            }
        }
    }

    private fun textStyle(): String = "display: flex; margin: 0; color: ${Colors.ON_DARK_BLUE};"
}
