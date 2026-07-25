package dev.gemmabcr.views.ui

import dev.gemmabcr.models.pokemons.BasePokemon
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.grid
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.style

class PokemonCard(
    private val pokemon: BasePokemon,
    private val canEdit: Boolean,
) : UiComponent {
    private var child: () -> Unit = {}
    override fun create(content: FlowContent): FlowContent = content.apply {
        column {
            classes = setOf("ui-card")
            style = "width: 100%; overflow: hidden;"
            row(
                align = AlignItems.CENTER,
                style = "background-color: ${Colors.DARK_BLUE}; padding: 0.55rem 0.65rem;"
            ) {
                div {
                    style = "border: 1px solid ${Colors.CREAM}; border-radius: 6px; padding: 0.25rem 0.4rem;"
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
            grid("minmax(160px, 240px) minmax(0, 1fr)") {
                classes = setOf("pokemon-card-body")
                column(JustifyContent.CENTER, AlignItems.CENTER, style = "padding: 1rem") {
                    PokemonImage(pokemon.generalId).create(this)
                    child()
                }
                ToDos(pokemon.hisuiId, pokemon.toDos, canEdit).create(this)
            }
        }
    }

    private fun textStyle(): String = "display: flex; margin: 0; color: ${Colors.ON_DARK_BLUE};"

    fun with(child: () -> Unit) = apply {
        this.child = child
    }
}
