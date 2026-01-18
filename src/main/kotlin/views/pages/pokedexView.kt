package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.views.ui.pokemonImage
import kotlinx.html.BODY
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.tr

fun BODY.pokedexView(pokemons: List<Pokemon>) {
    h1 { +"Pokédex tracking list" }
    h2 { +"Pokémons de Hisui" }
    div {
        style = "display: flex; flex-direction: column; gap: 2rem; width: 100%;"
        div {
            style = "display: flex; justify-content: space-between;"
            div {
                label { +"search by name" }
                input(type = InputType.text, name = "name")
            }
            div {
                label { +"search by number" }
                input(type = InputType.number, name = "search by number")
            }
        }
        pokemons.map { pokemon ->
            div {
                style =
                    "background-color: #F2F0E3; border-radius: 0 0 1rem 1rem; box-shadow: rgba(0, 0, 0, 0.16) 0 1px 4px; display: grid; grid-template-columns: 240px 1fr; width: 100%;"
                div {
                    style =
                        "display: flex; align-items: center; justify-content: center; border-top: 1px solid #D8D2AB; flex-direction: column; gap: 0.5rem; padding: 1rem;"
                    h3 {
                        style = "margin: 0px; color: #1A4A63;"
                        +"#${pokemon.hisuiId} ${pokemon.name}"
                    }
                    pokemonImage(pokemon.generalId)
                    div {
                        style = "display: flex;"
                        pokemon.types.map {
                            div {
                                style =
                                    "display: flex; justify-content: center; background-color: #3d3d3d; border-radius: 0.25rem; color: #fff; margin-right: 0.5rem; padding: 0.25rem 0.5rem;"
                                img(src = "/icons/types/${it.name.lowercase()}.svg") {
                                    height = "16"
                                    width = "16"
                                    style = "background-color: #eee; border-radius: 0.25rem; margin-right: 0.5rem;"
                                }
                                +it.text
                            }
                        }
                    }
                    a(href = "/${pokemon.hisuiId}") {
                        style =
                            "border: 2px solid #3695bb; border-radius: 0.25rem; color: #1684b0; cursor: pointer; font-weight: 600; justify-content: center; padding: 0.25rem 0.5rem;"
                        +"Ver más info"
                    }
                }
                div {
                    style =
                        "display: flex; align-items: center; border-top: 1px solid #D8D2AB; flex-direction: column; gap: 0.5rem; padding: 1rem;"
                    h4 {
                        style = "margin: 0px; color: #1A4A63;"
                        +"Tareas de la Pokédex"
                    }
                    h5 {
                        style = "margin: 0px; color: #1A4A63;"
                        +"En progreso (0/${pokemon.toDos.size})"
                    }
                    table {
                        tbody {
                            tr {
                                th { +"Progreso" }
                                th { +"Descripción" }
                            }
                            pokemon.toDos.map {
                                tr {
                                    td { +"0 of ${it.goal}" }
                                    td { +it.description }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
