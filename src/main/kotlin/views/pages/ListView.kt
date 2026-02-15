package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.buttonLink
import dev.gemmabcr.views.pages.components.pokemonCard
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.flexs.AlignItems
import dev.gemmabcr.views.ui.flexs.Gap
import dev.gemmabcr.views.ui.flexs.JustifyContent
import dev.gemmabcr.views.ui.flexs.column
import dev.gemmabcr.views.ui.form
import dev.gemmabcr.views.ui.numberInput
import dev.gemmabcr.views.ui.textInput
import dev.gemmabcr.views.ui.flexs.row
import dev.gemmabcr.views.ui.selectInput
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FormMethod
import kotlinx.html.button
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.style

class ListView(criteria: QueryCriteria, pokemons: List<Pokemon>) : HtmlLayout("Pokémons de Hisui", {
    column(gap = Gap.MAX) {
        val autoSubmit =
            "document.getElementById('page-input').value='1'; Array.from(this.form.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true}); this.form.submit()"
        form(
            action = "/pokemons",
            method = FormMethod.get,
            submitText = "Filter",
            id = "filter-form",
            onSubmit = "Array.from(this.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true})"
        ) {
            input(name = QueryCriteriaType.PAGE.key()) {
                id = "page-input"
                style = "display: none;"
                value = criteria.pagination.page.toString()
            }
            row(
                JustifyContent.SPACE_BETWEEN,
                AlignItems.END,
                style = "width: 100%; margin-top: 1rem; flex-wrap: wrap;"
            ) {
                selectInput(
                    "Area",
                    QueryCriteriaType.AREA.key(),
                    listOf("All") + Area.entries.map { it.name },
                    value = criteria.area?.name ?: "All",
                    onChange = autoSubmit
                )
                textInput(
                    "Search by name",
                    QueryCriteriaType.NAME.key(),
                    value = criteria.name,
                    onChange = autoSubmit
                )
                numberInput(
                    "Search by number",
                    QueryCriteriaType.NUMBER.key(),
                    criteria.number?.toString(),
                    onChange = autoSubmit
                )
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
            pokemonCard(pokemon) {
                row(JustifyContent.CENTER, style = "padding: 1rem;") {
                    buttonLink("/pokemons/${pokemon.hisuiId}", "Ver más info")
                }
            }
        }
        row(JustifyContent.CENTER, gap = Gap.MAX) {
            if (criteria.pagination.page > 1) {
                paginationButton("Previous", criteria.pagination.page - 1)
            }
            if (pokemons.size == criteria.pagination.pageSize) {
                paginationButton("Next", criteria.pagination.page + 1)
            }
        }
    }
})

private fun DIV.paginationButton(text: String, toPage: Int) {
    button(type = ButtonType.button) {
        style =
            "background-color: ${Colors.CREAM}; border: none; padding: 0.5rem 1rem; border-radius: 4px; cursor: pointer; font-weight: bold; color: ${Colors.DARK_BLUE};"
        this.onClick =
            "document.getElementById('page-input').value = '${toPage}'; document.getElementById('filter-form').submit()"
        +text
    }
}
