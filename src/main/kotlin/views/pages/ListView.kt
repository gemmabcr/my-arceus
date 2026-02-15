package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Area
import dev.gemmabcr.models.Pokemon
import dev.gemmabcr.models.QueryCriteria
import dev.gemmabcr.views.QueryCriteriaType
import dev.gemmabcr.views.i18n.I18nKey
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

class ListView(private val criteria: QueryCriteria, private val pokemons: List<Pokemon>) :
    HtmlLayout(I18nKey.LIST) {
    override fun DIV.content() {
        column(gap = Gap.MAX) {
            val autoSubmit =
                "document.getElementById('page-input').value='1'; Array.from(this.form.elements).forEach(e=>{if(e.name&&!e.value)e.disabled=true}); this.form.submit()"
            form(
                action = "/pokemons",
                method = FormMethod.get,
                submitText = translate(I18nKey.FILTER),
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
                        translate(I18nKey.AREA),
                        QueryCriteriaType.AREA.key(),
                        listOf(translate(I18nKey.ALL)) + Area.entries.map { it.name },
                        value = criteria.area?.name ?: translate(I18nKey.ALL),
                        onChange = autoSubmit
                    )
                    textInput(
                        translate(I18nKey.NAME),
                        QueryCriteriaType.NAME.key(),
                        value = criteria.name,
                        onChange = autoSubmit
                    )
                    numberInput(
                        translate(I18nKey.NUMBER),
                        QueryCriteriaType.NUMBER.key(),
                        criteria.number?.toString(),
                        onChange = autoSubmit
                    )
                }
            }
            if (pokemons.isEmpty()) {
                row(JustifyContent.CENTER) {
                    p { +translate(I18nKey.NO_RESULTS) }
                    img(src = "/icons/not_found.png") {
                        height = "120"
                        width = "120"
                    }
                }
            }
            pokemons.forEach { pokemon ->
                pokemonCard(pokemon) {
                    row(JustifyContent.CENTER, style = "padding: 1rem;") {
                        buttonLink("/pokemons/${pokemon.hisuiId}", translate(I18nKey.MORE_INFO))
                    }
                }
            }
            row(JustifyContent.CENTER, gap = Gap.MAX) {
                if (criteria.pagination.page > 1) {
                    paginationButton(translate(I18nKey.PREVIOUS), criteria.pagination.page - 1)
                }
                if (pokemons.size == criteria.pagination.pageSize) {
                    paginationButton(translate(I18nKey.NEXT), criteria.pagination.page + 1)
                }
            }
        }
    }

    private fun DIV.paginationButton(text: String, toPage: Int) {
        button(type = ButtonType.button) {
            style =
                "background-color: ${Colors.CREAM}; border: none; padding: 0.5rem 1rem; " +
                        "border-radius: 4px; cursor: pointer; font-weight: bold; color: ${Colors.DARK_BLUE};"
            this.onClick =
                "document.getElementById('page-input').value = '${toPage}'; document.getElementById('filter-form').submit()"
            +text
        }
    }
}
