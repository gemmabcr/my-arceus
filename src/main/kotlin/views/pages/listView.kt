package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.components.toDos
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.ui.card
import dev.gemmabcr.views.ui.h1
import dev.gemmabcr.views.ui.h2
import dev.gemmabcr.views.ui.h3
import dev.gemmabcr.views.ui.form
import dev.gemmabcr.views.ui.button
import dev.gemmabcr.views.ui.numberInput
import dev.gemmabcr.views.ui.textInput
import dev.gemmabcr.views.ui.pokemonImage
import dev.gemmabcr.views.ui.row
import dev.gemmabcr.views.ui.typeChips
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.onChange
import kotlinx.html.onSubmit
import kotlinx.html.p
import kotlinx.html.style

fun DIV.listView(criteria: QueryCriteria, pokemons: List<Pokemon>) {
    h1("Pokédex tracking list")
    h2("Pokémons de Hisui")
    div {
        style = "display: flex; flex-direction: column; gap: 2rem; width: 100%;"
        form {
            onSubmit = "Array.from(this.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
            div {
                style = "display: flex; gap: 0.5rem; flex-wrap: wrap;"
                Area.entries.forEach { area ->
                    val inputId = "area-${area.name}"
                    input(type = InputType.radio, name = QueryCriteriaType.AREA.key()) {
                        id = inputId
                        value = area.name
                        checked = area == criteria.area
                        style = "display: none;"
                        onChange =
                            "Array.from(this.form.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true}); this.form.submit()"
                    }
                    label {
                        htmlFor = inputId
                        style =
                            "padding: 0.5rem 1rem; border: 1px solid #D8D2AB; border-radius: 20px; cursor: pointer; background-color: ${if (area == criteria.area) "#D8D2AB" else "white"}; color: ${if (area == criteria.area) "white" else "black"}; transition: 0.3s;"
                        +area.text
                    }
                }
            }
            row("justify-content: space-between") {
                textInput("search by name", QueryCriteriaType.NAME.key(), value = criteria.name)
                numberInput(
                    "search by number",
                    QueryCriteriaType.NUMBER.key(),
                    criteria.number?.toString()
                )
                button("Filter")
            }
        }
        if (pokemons.isEmpty().not()) {
            row("justify-content: center") {
                p { +"No pokemons found" }
                img(src = "/icons/not_found.png") {
                    height = "120"
                    width = "120"
                    style = "background-color: #eee; border-radius: 0.25rem; margin-right: 0.5rem;"
                }
            }
        }
        pokemons.forEach { pokemon ->
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
                        buttonLink("/pokemons/${pokemon.hisuiId}", "Ver más info")
                    }
                    toDos(pokemon.toDos)
                }
            }
        }
    }
}
