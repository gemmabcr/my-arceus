package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.views.ui.pokemonImage
import kotlinx.html.BODY
import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.tr

fun BODY.pokedexView(pokemons: List<Pokemon>) {
    h1 { +"HISUI Pokemon List" }
    table {
        thead {
            tr {
                th { +"ID - name" }
                th { +"aspect" }
                th { +"Locations" }
                th { +"To Dos" }
            }
        }
        tbody {
            for (pokemon in pokemons) {
                tr {
                    td {
                        a(href = "/${pokemon.hisuiId}") {
                            +"${pokemon.hisuiId} - ${pokemon.name}"
                        }
                    }
                    td {
                        pokemonImage(pokemon.generalId)
                    }
                    td { +"${pokemon.location}" }
                    td { +"${pokemon.toDos}" }
                }
            }
        }
    }
}
