package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.views.components.toDos
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.card
import dev.gemmabcr.views.ui.h1
import dev.gemmabcr.views.ui.h2
import dev.gemmabcr.views.ui.h3
import dev.gemmabcr.views.ui.input
import dev.gemmabcr.views.ui.pokemonImage
import dev.gemmabcr.views.ui.row
import dev.gemmabcr.views.ui.typeChips
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.style

fun DIV.pokedexView(pokemons: List<Pokemon>) {
    h1("Pokédex tracking list")
    h2("Pokémons de Hisui")
    div {
        style = "display: flex; flex-direction: column; gap: 2rem; width: 100%;"
        row("justify-content: space-between") {
            input("search by name", "name")
            input("search by number", "number", InputType.number)
        }
        pokemons.map { pokemon ->
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
                        buttonLink("/${pokemon.hisuiId}", "Ver más info")
                    }
                    toDos(pokemon.toDos)
                }
            }
        }
    }
}
