package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.pages.components.pokemonCard
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.h2
import dev.gemmabcr.views.ui.form
import dev.gemmabcr.views.ui.button as uiButton
import dev.gemmabcr.views.ui.numberInput
import dev.gemmabcr.views.ui.textInput
import dev.gemmabcr.views.ui.flexs.row
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.onChange
import kotlinx.html.onClick
import kotlinx.html.onSubmit
import kotlinx.html.p
import kotlinx.html.style

fun DIV.listView(criteria: QueryCriteria, pokemons: List<Pokemon>) {
    h2("Pokémons de Hisui")
    column {
        form {
            id = "filter-form"
            onSubmit = "Array.from(this.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
            input(type = InputType.hidden, name = QueryCriteriaType.PAGE.key()) {
                id = "page-input"
                value = criteria.pagination.page.toString()
            }
            row(style = "flex-wrap: wrap;") {
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
                            "padding: 0.5rem 1rem; border: 1px solid ${Colors.CREAM.hex}; border-radius: 20px; cursor: pointer; background-color: ${if (area == criteria.area) "#D8D2AB" else "white"}; color: ${if (area == criteria.area) "white" else "black"}; transition: 0.3s;"
                        +area.text
                    }
                }
            }
            row(JustifyContent.SPACE_BETWEEN, AlignItems.CENTER, style = "width: 100%;") {
                textInput("search by name", QueryCriteriaType.NAME.key(), value = criteria.name)
                numberInput(
                    "search by number",
                    QueryCriteriaType.NUMBER.key(),
                    criteria.number?.toString()
                )
                uiButton("Filter")
            }
        }
        if (pokemons.isEmpty()) {
            row(JustifyContent.CENTER) {
                p { +"No pokemons found" }
                img(src = "/icons/not_found.png") {
                    height = "120"
                    width = "120"
                }
            }
        }
        pokemons.forEach { pokemon ->
            pokemonCard(
                pokemon,
                leftColumnItem = { buttonLink("/pokemons/${pokemon.hisuiId}", "Ver más info") },
            )
        }
        row(JustifyContent.CENTER, gap = Gap.MAX) {
            if (criteria.pagination.page > 1) {
                paginationButton(
                    "Previous",
                    criteria.pagination.page - 1
                )
            }
            if (pokemons.size == criteria.pagination.pageSize) {
                paginationButton(
                    "Next",
                    criteria.pagination.page + 1
                )
            }
        }
    }
}

private fun DIV.paginationButton(text: String, toPage: Int) {
    button(type = ButtonType.button) {
        style =
            "background-color: ${Colors.CREAM.hex}; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer; font-weight: bold; color: #4A4A4A;"
        this.onClick =
            "document.getElementById('page-input').value = '${toPage}'; document.getElementById('filter-form').submit()"
        +text
    }
}
