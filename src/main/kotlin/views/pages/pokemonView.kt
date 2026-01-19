package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Location
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.SpecialCondition
import dev.gemmabcr.views.components.toDos
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.card
import dev.gemmabcr.views.ui.h1
import dev.gemmabcr.views.ui.h2
import dev.gemmabcr.views.ui.h3
import dev.gemmabcr.views.ui.h4
import dev.gemmabcr.views.ui.h5
import dev.gemmabcr.views.ui.pokemonImage
import dev.gemmabcr.views.ui.row
import dev.gemmabcr.views.ui.typeChips
import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.p
import kotlinx.html.style

fun DIV.pokemonView(pokemon: Pokemon) {
    h1("Pokédex tracking list - Hisui")
    h2("Pokémon detail")
    row {
        buttonLink("/", "< Atrás")
    }
    card {
        div {
            style = "display: grid; grid-template-columns: 240px 1fr;"
            div {
                style =
                    "display: flex; align-items: center; justify-content: center; border-top: 1px solid #D8D2AB; flex-direction: column; gap: 0.5rem; padding: 1rem;"
                h3("#${pokemon.hisuiId} ${pokemon.name}", margin = false)
                pokemonImage(pokemon.generalId)
                typeChips(
                    pokemon.types.map { it.name.lowercase() to it.text }
                )
            }
            div {
                style =
                    "display: flex; align-items: center; border-left: 1px solid #D8D2AB; flex-direction: column; gap: 0.5rem; padding: 1rem;"
                h4("Localización", margin = false)
                val areas: Map<Area, List<Location>> = pokemon.location.groupBy { it.area }
                div {
                    style = "display: grid; grid-template-columns: repeat(3, 1fr);"
                    areas.map {
                        div {
                            style =
                                "align-items: center; border: 1px solid #D8D2AB; border-radius: 1rem; display: flex; flex-direction: column; gap: 0.5rem; padding: 1rem;"
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
                maybeCreateSpecialCondition(pokemon.specialCondition)
            }
        }
        toDos(pokemon.toDos)
    }
}

private fun DIV.maybeCreateSpecialCondition(specialCondition: SpecialCondition?) {
    if (specialCondition == null) {
        return
    }
    h4("Condiciones especiales", margin = false)
    p {
        +specialCondition.text
    }
}
