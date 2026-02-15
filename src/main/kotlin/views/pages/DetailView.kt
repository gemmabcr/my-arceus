package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.DetailedPokemon
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.SpecialCondition
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.pages.components.pokemonCard
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.borders.Border
import dev.gemmabcr.views.ui.borders.BorderRadius
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.grid
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import kotlinx.html.FlowContent
import kotlinx.html.p
import kotlinx.html.style

class DetailView(pokemon: DetailedPokemon) : HtmlLayout("Pokémon detail", {
    column(gap = Gap.MAX) {
        buttonLink("/", "< Atrás")
        pokemonCard(pokemon) {
            column(style = "padding: 1rem; border-top: 1px solid ${Colors.DARK_BLUE}") {
                h4("Localización", margin = false)
                val areas: Map<Area, List<Location>> = pokemon.location.groupBy { it.area }
                grid("repeat(3, 1fr)", style = "column-gap: 1rem;") {
                    areas.map {
                        column(style = "${Border(Colors.DARK_BLUE, radius = BorderRadius.MAX).text()}; padding: 1rem") {
                            h5(it.key.text, margin = false)
                            if (it.key != Area.DISTORTION) {
                                it.value.forEach { location ->
                                    p {
                                        style = "margin: 0px;"
                                        +location.name
                                    }
                                }
                            }
                        }
                    }
                }
            }
            maybeCreateSpecialCondition(pokemon.specialCondition)
        }
    }
})

private fun FlowContent.maybeCreateSpecialCondition(specialCondition: SpecialCondition?) {
    if (specialCondition == null) {
        return
    }
    column(style = "padding: 1rem; border-top: 1px solid ${Colors.DARK_BLUE}") {
        h4("Condiciones especiales", margin = false)
        p {
            +specialCondition.toString()
        }
    }
}
