package dev.gemmabcr.views.ui

import dev.gemmabcr.models.pokemons.Area
import dev.gemmabcr.models.pokemons.CaughtCondition
import dev.gemmabcr.models.pokemons.DetailedPokemon
import dev.gemmabcr.models.pokemons.Location
import dev.gemmabcr.views.adapters.AreaI18nKeyAdapter
import dev.gemmabcr.views.adapters.CaughtConditionI18nKeyAdapter
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.column
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.p
import kotlinx.html.style

class PokemonDetailContent(
    private val pokemon: DetailedPokemon,
    private val canEdit: Boolean,
) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        column(gap = Gap.MAX) {
            buttonLink("/", translate(CommonI18nKey.BACK))
            PokemonCard(pokemon, canEdit).create(this).apply {
                locations()
                specialCondition(pokemon.caughtCondition)
            }
        }
    }

    private fun FlowContent.locations() {
        column(style = SECTION_STYLE) {
            h4(translate(CommonI18nKey.LOCATION), margin = false)
            val areas: Map<Area, List<Location>> = pokemon.location.groupBy { it.area }
            grid("repeat(auto-fit, minmax(180px, 1fr))", style = "gap: 0.75rem;") {
                areas.forEach { (area, locations) ->
                    column(style = "padding: 1rem;") {
                        classes = setOf("ui-panel")
                        h5(translate(AreaI18nKeyAdapter(area).i18nKey()), margin = false)
                        if (area != Area.DISTORTION) {
                            locations.forEach { location ->
                                p {
                                    style = "margin: 0;"
                                    +location.name
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.specialCondition(condition: CaughtCondition?) {
        condition ?: return
        column(style = SECTION_STYLE) {
            h4(translate(CommonI18nKey.SPECIAL_CONDITION), margin = false)
            p { +translate(CaughtConditionI18nKeyAdapter(condition).i18nKey()) }
        }
    }
}

private val SECTION_STYLE = "padding: 1.25rem; border-top: 1px solid ${Colors.CREAM}"
